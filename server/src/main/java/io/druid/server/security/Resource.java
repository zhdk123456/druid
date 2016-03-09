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

package io.druid.server.security;

public class Resource
{
  private String name;
  private ResourceType type;

  public Resource(String name, ResourceType type)
  {
    this.name = name;
    this.type = type;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public ResourceType getType()
  {
    return type;
  }

  public void setType(ResourceType type)
  {
    this.type = type;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Resource resource = (Resource) o;

    if (!name.equals(resource.name)) {
      return false;
    }
    return type == resource.type;

  }

  @Override
  public int hashCode()
  {
    int result = name.hashCode();
    result = 31 * result + type.hashCode();
    return result;
  }
}