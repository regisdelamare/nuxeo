/*
 * (C) Copyright 2018 Nuxeo (http://nuxeo.com/) and others.
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
 *
 * Contributors:
 *     Funsho David
 */

package org.nuxeo.ecm.core.bulk.actions;

import java.util.Map;

import org.nuxeo.lib.stream.computation.Topology;
import org.nuxeo.lib.stream.computation.Topology.Builder;
import org.nuxeo.runtime.stream.StreamProcessorTopology;

/**
 * @since 10.3
 */
public abstract class AbstractBulkAction implements StreamProcessorTopology {

    public static final String BATCH_SIZE_OPT = "batchSize";

    public static final String BATCH_THRESHOLD_MS_OPT = "batchThresholdMs";

    public static final int DEFAULT_BATCH_SIZE = 10;

    public static final int DEFAULT_BATCH_THRESHOLD_MS = 200;

    @Override
    public Topology getTopology(Map<String, String> options) {
        int size = getOptionAsInteger(options, BATCH_SIZE_OPT, DEFAULT_BATCH_SIZE);
        int threshold = getOptionAsInteger(options, BATCH_THRESHOLD_MS_OPT, DEFAULT_BATCH_THRESHOLD_MS);
        return addComputations(Topology.builder(), size, threshold, options).build();
    }

    protected abstract Builder addComputations(Builder builder, int size, int threshold, Map<String, String> options);

    protected int getOptionAsInteger(Map<String, String> options, String option, int defaultValue) {
        String value = options.get(option);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

}