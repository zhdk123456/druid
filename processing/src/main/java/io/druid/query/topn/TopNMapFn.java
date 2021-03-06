/*
 * Licensed to Metamarkets Group Inc. (Metamarkets) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Metamarkets licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.druid.query.topn;

import io.druid.query.DataSourceQueryMetrics;
import io.druid.query.QueryMetricsContext;
import io.druid.query.Result;
import io.druid.segment.Cursor;
import io.druid.segment.DimensionSelector;

import javax.annotation.Nullable;

public class TopNMapFn
{

  private final TopNQuery query;
  private final TopNAlgorithm topNAlgorithm;

  public TopNMapFn(
      TopNQuery query,
      TopNAlgorithm topNAlgorithm
  )
  {
    this.query = query;
    this.topNAlgorithm = topNAlgorithm;
  }

  /**
   * @param cursor cursor over rows to process
   * @param first if this is a first call of apply() in a series of similar calls over different ranges of rows, to
   *              set some dimensions in queryMetricsContext and log some diagnostic things only once
   * @param queryMetricsContext "output parameter", to set some dimensions to, if the given first argument is true
   * @param dataSourceQueryMetrics to accumulate metrics to
   * @return
   */
  @SuppressWarnings("unchecked")
  public Result<TopNResultValue> apply(
      Cursor cursor,
      boolean first,
      @Nullable QueryMetricsContext queryMetricsContext,
      @Nullable DataSourceQueryMetrics dataSourceQueryMetrics
  )
  {
    final DimensionSelector dimSelector = cursor.makeDimensionSelector(
        query.getDimensionSpec()
    );
    if (dimSelector == null) {
      return null;
    }

    TopNParams params = null;
    try {
      params = topNAlgorithm.makeInitParams(dimSelector, cursor);
      if (first && queryMetricsContext != null) {
        queryMetricsContext.setDimension("firstDimensionSelector", dimSelector.getDimensionSelectorType());
        dataSourceQueryMetrics.dimensionSelector = dimSelector;
        long numValuesPerPass = QueryMetricsContext.roundToPowerOfTwo(params.getNumValuesPerPass());
        queryMetricsContext.setDimension("numValuesPerPass", numValuesPerPass);
      }

      TopNResultBuilder resultBuilder = BaseTopNAlgorithm.makeResultBuilder(params, query);

      topNAlgorithm.run(params, null, resultBuilder, dataSourceQueryMetrics);

      return resultBuilder.build();
    }
    finally {
      topNAlgorithm.cleanup(params);
    }
  }
}
