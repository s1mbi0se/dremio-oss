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
<@pp.dropOutputFile />

<@pp.changeOutputFile name="/com/dremio/exec/expr/fn/impl/G{}{type}ToMD5.java" />

<#include "/@includes/license.ftl" />

package com.dremio.exec.expr.fn.impl;

<#include "/@includes/vv_imports.ftl" />

import com.dremio.exec.expr.SimpleFunction;
import com.dremio.exec.expr.annotations.FunctionTemplate;
import com.dremio.exec.expr.annotations.FunctionTemplate.NullHandling;
import com.dremio.exec.expr.annotations.Output;
import com.dremio.exec.expr.annotations.Workspace;
import com.dremio.exec.expr.annotations.Param;
import com.dremio.exec.expr.fn.FunctionErrorContext;
import org.apache.arrow.vector.holders.*;

import io.netty.buffer.ByteBuf;
import org.apache.arrow.memory.ArrowBuf;
import javax.inject.Inject;

/**
 * generated from ${.template_name} ${type}
 */

@SuppressWarnings("unused")
@FunctionTemplate(name = "hashMD5" , scope = FunctionTemplate.FunctionScope.SIMPLE, nulls = NullHandling.NULL_IF_NULL)
public class G${type}ToMD5 implements SimpleFunction{
    @Param  ${type}Holder in;
    @Inject ArrowBuf buffer;
    @Output VarCharHolder out;
    @Inject FunctionErrorContext errCtx;

  @Override
  public void setup() {
    }

  @Override
  public void eval() {
<#if ${type} == "BigInt">
    byte[] buf = Long.toBinaryString(in.value).getBytes();
</#if>
<#elseif ${type} == "Decimal">
  byte[] buf = com.dremio.common.util.DremioStringUtils.toBinaryStringNoFormat(io.netty.buffer.NettyArrowBuf.unwrapBuffer(in.buffer), (int) in
  .start, 16).getBytes();
</#elseif>
<#elseif ${type} == "Float4">
  byte[] buf = Float.toBinaryString(in.value).getBytes();
</#elseif>
<#elseif ${type} == "Float8">
  byte[] buf = Double.toBinaryString(in.value).getBytes();
</#elseif>
<#else">
  byte[] buf = ${type}.toBinaryString(in.value).getBytes();
</#else>
    buffer.setBytes(0, DigestUtils.md5(buf));

    out.start = 0;
    out.end = buf.length;
    out.buffer = buffer;
    }
}
