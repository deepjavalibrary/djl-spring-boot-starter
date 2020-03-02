/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package ai.djl.spring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("djl")
public class DjlConfigurationProperties {

    /**
     * Criteria used as filters for model lookup.
     * For more information on available criteria that are currently part of the repository see
     * https://github.com/awslabs/djl/tree/master/mxnet/mxnet-model-zoo
     */
    private Map<String, String> modelCriteria;

    /**
     * Loader type to use. By default set to SSD (single shot detection).
     */
    private MxModelZooType mxModelZooType;

    public Map<String, String> getModelCriteria() {
        return modelCriteria;
    }

    public void setModelCriteria(Map<String, String> modelCriteria) {
        this.modelCriteria = modelCriteria;
    }

    public MxModelZooType getMxModelZooType() {
        return mxModelZooType;
    }

    public void setMxModelZooType(MxModelZooType mxModelZooType) {
        this.mxModelZooType = mxModelZooType;
    }
}
