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

package io.druid.query.aggregation.distinctcount;

import com.metamx.collections.bitmap.MutableBitmap;
import io.druid.query.aggregation.Aggregator;
import io.druid.segment.DimensionSelector;

public class DistinctCountAggregator extends Aggregator
{

  private final String name;
  private final DimensionSelector selector;
  private final MutableBitmap mutableBitmap;

  public DistinctCountAggregator(
      String name,
      DimensionSelector selector,
      MutableBitmap mutableBitmap
  )
  {
    this.name = name;
    this.selector = selector;
    this.mutableBitmap = mutableBitmap;
  }

  @Override
  public void aggregate()
  {
    for (final Integer index : selector.getRow()) {
      mutableBitmap.add(index);
    }
  }

  @Override
  public void reset()
  {
    mutableBitmap.clear();
  }

  @Override
  public Object get()
  {
    return mutableBitmap.size();
  }

  @Override
  public float getFloat()
  {
    return (float) mutableBitmap.size();
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void close()
  {
    mutableBitmap.clear();
  }

  @Override
  public long getLong()
  {
    return (long) mutableBitmap.size();
  }

  @Override
  public String getAggregatorType()
  {
    return getClass().getName() + "["
           + "selector=" + selector.getDimensionSelectorType()
           + ", mutableBitmap=" + mutableBitmap.getClass().getName()
           + "]";
  }
}
