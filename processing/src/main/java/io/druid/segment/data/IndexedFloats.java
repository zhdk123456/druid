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

package io.druid.segment.data;

import java.io.Closeable;

/**
 * Get a float at an index (array or list lookup abstraction without boxing).
 */
public abstract class IndexedFloats implements Closeable
{
  public abstract int size();
  public abstract float get(int index);

  public final void fill(int index, float[] toFill)
  {
    if (size() - index < toFill.length) {
      throw new IndexOutOfBoundsException(
          String.format(
              "Cannot fill array of size[%,d] at index[%,d].  Max size[%,d]", toFill.length, index, size()
          )
      );
    }
    for (int i = 0; i < toFill.length; i++) {
      toFill[i] = get(index + i);
    }
  }

  @Override
  public abstract void close();
}
