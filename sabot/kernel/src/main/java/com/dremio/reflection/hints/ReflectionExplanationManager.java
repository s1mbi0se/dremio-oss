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
package com.dremio.reflection.hints;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.calcite.rel.metadata.RelColumnOrigin;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dremio.reflection.hints.features.FieldMissingFeature;
import com.dremio.reflection.hints.features.FilterDisjointFeature;
import com.dremio.reflection.hints.features.HintFeature;
import com.dremio.reflection.hints.features.MaterializationFilterOverSpecifiedFeature;
import com.dremio.sabot.kernel.proto.DisjointFilterExplanation;
import com.dremio.sabot.kernel.proto.FieldMissingExplanation;
import com.dremio.sabot.kernel.proto.FilterOverSpecifiedExplanation;
import com.dremio.sabot.kernel.proto.ReflectionExplanation;
import com.dremio.sabot.kernel.proto.ReflectionExplanationType;

public class ReflectionExplanationManager {
  public static final int MAX_REFLECTIONS_TO_DISPLAY_TO_SHOW = 5;
  public static final int MAX_NUMBER_OF_HINTS_PER_REFLECTION = 5;
  private final static Logger LOGGER = LoggerFactory.getLogger(ReflectionExplanationManager.class);
  private final ReflectionExplanationFeatureGatherer reflectionExplanationFeatureGatherer;

  public ReflectionExplanationManager(ReflectionExplanationFeatureGatherer reflectionExplanationFeatureGatherer) {
    this.reflectionExplanationFeatureGatherer = reflectionExplanationFeatureGatherer;
  }

  public Stream<ReflectionExplanationsAndQueryDistance> generateDisplayExplanations() {
    return reflectionExplanationFeatureGatherer.reflectionIdToFeatureList.entrySet()
      .stream()
      .filter(e -> e.getValue().size() <= MAX_NUMBER_OF_HINTS_PER_REFLECTION)
      .map(this::entryToDisplayHint)
      .filter(this::nonZeroDistance)
      .sorted()
      .limit(MAX_REFLECTIONS_TO_DISPLAY_TO_SHOW)
      .peek(this::decorateWithDisplayMessages);
  }

  private ReflectionExplanationsAndQueryDistance entryToDisplayHint(
    Entry<String, Set<HintFeature>> reflectionIdAndFeatureSet) {
    return new ReflectionExplanationsAndQueryDistance(reflectionIdAndFeatureSet.getKey(),
      queryDistance(reflectionIdAndFeatureSet.getValue()));
  }

  private void decorateWithDisplayMessages(ReflectionExplanationsAndQueryDistance reflectionExplanationsAndQueryDistance) {
    String reflectionId = reflectionExplanationsAndQueryDistance.reflectionId;
    Set<HintFeature> hintFeatureSet = reflectionExplanationFeatureGatherer.reflectionIdToFeatureList.get(reflectionId);
    reflectionExplanationsAndQueryDistance.displayHintMessageList = hintFeatureSet
      .stream()
      .map(this::featureToExplanation)
      .collect(toImmutableList());
  }

  private ReflectionExplanation featureToExplanation(HintFeature hintFeature) {
    if(hintFeature instanceof FieldMissingFeature){
      return new ReflectionExplanation()
          .setExplanation(ReflectionExplanationType.FIELD_MISSING)
          .setFieldMissing(fieldMissingToDisplayHintMessage((FieldMissingFeature) hintFeature));
    } else if(hintFeature instanceof MaterializationFilterOverSpecifiedFeature) {
      return new ReflectionExplanation()
          .setExplanation(ReflectionExplanationType.FILTER_OVER_SPECIFIED)
          .setFilterOverSpecified(featureToExplanation((MaterializationFilterOverSpecifiedFeature)hintFeature));
    } else if(hintFeature instanceof FilterDisjointFeature) {
      return new ReflectionExplanation()
          .setExplanation(ReflectionExplanationType.DISJOINT_FILTER)
          .setDisjointFilter(clauseToDisplayHintMessage((FilterDisjointFeature) hintFeature));
    } else {
      throw new RuntimeException("Unknown Type" + hintFeature.getClass());
    }
  }

  private DisjointFilterExplanation clauseToDisplayHintMessage(FilterDisjointFeature filterDisjointFeature) {
    return new DisjointFilterExplanation()
        .setFilter(filterDisjointFeature.getMaterializationFilter().toString());
  }

  private FieldMissingExplanation fieldMissingToDisplayHintMessage(FieldMissingFeature fieldMissingFeature){
    try {
      RelMetadataQuery metadataQuery = fieldMissingFeature.getUserQueryNode().getCluster().getMetadataQuery();
      RelColumnOrigin columnOrigin = metadataQuery.getColumnOrigin(fieldMissingFeature.getUserQueryNode(), fieldMissingFeature.getIndex());
      return new FieldMissingExplanation()
        .setColumnName(columnOrigin.toString())
        .setColumnIndex(fieldMissingFeature.getIndex());
    } catch (Exception ex) {
      LOGGER.warn("Failed to create display data",  ex);
      return new FieldMissingExplanation()
        .setColumnName(fieldMissingFeature.getName())
        .setColumnIndex(fieldMissingFeature.getIndex());
    }
  }

  private FilterOverSpecifiedExplanation featureToExplanation(
      MaterializationFilterOverSpecifiedFeature materializationFilterOverSpecified) {
    return new FilterOverSpecifiedExplanation()
        .setFilter(materializationFilterOverSpecified.getMaterializationFilter().toString());
  }

  private boolean nonZeroDistance(ReflectionExplanationsAndQueryDistance reflectionExplanationsAndQueryDistance) {
    return reflectionExplanationsAndQueryDistance.queryDistance != 0.0;
  }

  private double queryDistance(Set<HintFeature> hintFeatureSet) {
    return hintFeatureSet.size();
  }
}