/*
 * Copyright (C) 2017-2019 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.sabot.op.sort.external;

import java.io.IOException;
import java.util.List;

import org.apache.arrow.memory.ArrowBuf;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.OutOfMemoryException;
import org.apache.arrow.vector.types.pojo.Schema;

import com.dremio.common.AutoCloseables;
import com.dremio.exec.exception.ClassTransformationException;
import com.dremio.exec.exception.SchemaChangeException;
import com.dremio.exec.expr.ClassGenerator;
import com.dremio.exec.expr.ClassProducer;
import com.dremio.exec.expr.CodeGenerator;
import com.dremio.exec.physical.config.ExternalSort;
import com.dremio.exec.record.ExpandableHyperContainer;
import com.dremio.exec.record.RecordBatchData;
import com.dremio.exec.record.VectorAccessible;
import com.dremio.exec.record.selection.SelectionVector2;
import com.dremio.exec.record.selection.SelectionVector4;
import com.google.common.collect.Lists;

import io.netty.buffer.NettyArrowBuf;

/**
 * - Sort each batch locally using an SV2
 * - Insert each batch into a SplayTree as it arrives, final sorted list is generated by
 *   traversing this SplayTree.
 */
public class SplaySorter implements Sorter {
  private final ExternalSort sortConfig;
  private final ClassProducer classProducer;
  private final Schema schema;
  private final BufferAllocator allocator;

  private SingleBatchSorterInterface localSorter;
  private SplaySorterInterface treeManager;
  private ArrowBuf splayTreeBuffer;

  public SplaySorter(ExternalSort sortConfig, ClassProducer classProducer, Schema schema, BufferAllocator allocator) {
    this.sortConfig = sortConfig;
    this.classProducer = classProducer;
    this.schema = schema;
    this.allocator = allocator;
    this.splayTreeBuffer = allocator.buffer(4096 * SplayTree.NODE_SIZE);
    splayTreeBuffer.setZero(0, splayTreeBuffer.capacity());
  }

  public boolean expandMemoryIfNecessary(int newRequiredSize) {
    try {
      // Expand the SplayTree buffer, double size each time.
      final int requiredSize = (newRequiredSize + 1) * SplayTree.NODE_SIZE;
      while (splayTreeBuffer.capacity() < requiredSize) {
        final ArrowBuf oldSplayTree = splayTreeBuffer;
        this.splayTreeBuffer = allocator.buffer(splayTreeBuffer.capacity() * 2);
        splayTreeBuffer.setBytes(0, oldSplayTree, 0, oldSplayTree.capacity());
        splayTreeBuffer.setZero(oldSplayTree.capacity(), splayTreeBuffer.capacity() - oldSplayTree.capacity());
        if (treeManager != null) {
          treeManager.setDataBuffer(splayTreeBuffer);
        }
        oldSplayTree.close();
      }
    } catch (OutOfMemoryException ex) {
      return false;
    }

    return true;
  }

  public void setup(VectorAccessible batch) throws ClassTransformationException, SchemaChangeException, IOException {
    // Compile sorting classes.
    { // Local (single batch) sorter
      CodeGenerator<SingleBatchSorterInterface> cg =
        classProducer.createGenerator(SingleBatchSorterInterface.TEMPLATE_DEFINITION);
      ClassGenerator<SingleBatchSorterInterface> g = cg.getRoot();
      ExternalSortOperator.generateComparisons(g, batch, sortConfig.getOrderings(), classProducer);
      this.localSorter = cg.getImplementationClass();
    }

    { // Tree sorter
      CodeGenerator<SplaySorterInterface> cg = classProducer.createGenerator(SplaySorterInterface.TEMPLATE_DEFINITION);
      ClassGenerator<SplaySorterInterface> g = cg.getRoot();
      final Sv4HyperContainer container = new Sv4HyperContainer(allocator, schema);
      ExternalSortOperator.generateComparisons(g, container, sortConfig.getOrderings(), classProducer);
      this.treeManager = cg.getImplementationClass();
      treeManager.init(classProducer.getFunctionContext(), container);
      treeManager.setDataBuffer(splayTreeBuffer);
    }
  }

  public void addBatch(RecordBatchData data, BufferAllocator copyTargetAllocator) throws SchemaChangeException {
    // We now generate an sv2 for the local sort. We do this even if the incoming
    // batch has an sv2. This is because we need to treat that one as immutable.
    //
    // Note that we shouldn't have an issue with allocation here since we'll use
    // the copyTargetAllocator provided by the caller (it is used for the final
    // sort but it isn't yet used and is guaranteed to be larger than the size of
    // this // ephemeral allocation).
    try (SelectionVector2 localSortVector = new SelectionVector2(copyTargetAllocator)) {

      final int recordCount = data.getRecordCount();
      localSortVector.allocateNew(recordCount);
      final SelectionVector2 incomingSv2 = data.getSv2();
      if (incomingSv2 != null) {
        // just copy the sv2.
        NettyArrowBuf buffer = localSortVector.getBuffer(false).asNettyBuffer();
        buffer.arrowBuf().setBytes(buffer.writerIndex(), incomingSv2.getBuffer(false), 0,
          recordCount * 2);
      } else {
        for (int i = 0; i < recordCount; i++) {
          localSortVector.setIndex(i * 2, i);
        }
      }

      // Quicksort for cache-local and SplayTree performance benefits (includes resetting vector references)
      localSorter.setup(classProducer.getFunctionContext(), localSortVector, data.getContainer());
      localSorter.sort(localSortVector);

      // now we need to insert the values into the splay tree.
      treeManager.add(localSortVector, data);
    }
  }

  public ExpandableHyperContainer getHyperBatch() {
    if (treeManager != null) {
      return treeManager.getHyperBatch();
    } else {
      return null;
    }
  }

  public int getHyperBatchSize() {
    if (treeManager != null) {
      return treeManager.getHyperBatch().size();
    } else {
      return 0;
    }
  }

  public SelectionVector4 getFinalSort(BufferAllocator copyTargetAllocator, int targetBatchSize) {
    return treeManager.getFinalSort(copyTargetAllocator, targetBatchSize);
  }

  public void close() throws Exception {
    final List<AutoCloseable> closeables = Lists.newArrayList();

    closeables.add(splayTreeBuffer);
    AutoCloseables.close(closeables);

    splayTreeBuffer = null;
  }
}