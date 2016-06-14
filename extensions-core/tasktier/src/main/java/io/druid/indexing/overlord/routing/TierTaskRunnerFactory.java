/*
 * Licensed to Metamarkets Group Inc. (Metamarkets) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Metamarkets licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.druid.indexing.overlord.routing;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.druid.indexing.overlord.ForkingTaskRunnerFactory;
import io.druid.indexing.overlord.RemoteTaskRunnerFactory;
import io.druid.indexing.overlord.TaskRunner;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")

/**
 * A factory for building task runners for Tiering. This differs from the default TaskRunnerFactory because these are
 * creatable from JSON, whereas the default ones are only creatable via Guice injection
 */
@JsonSubTypes(value = {
    @JsonSubTypes.Type(name = UnknownRouteFactory.TYPE_NAME, value = UnknownRouteFactory.class),
    @JsonSubTypes.Type(name = RemoteTaskRunnerFactory.TYPE_NAME, value = RemoteTaskRunnerTierFactory.class),
    @JsonSubTypes.Type(name = ForkingTaskRunnerFactory.TYPE_NAME, value = ForkingTaskRunnerTierFactory.class)
})
public interface TierTaskRunnerFactory
{
  TaskRunner build();
}