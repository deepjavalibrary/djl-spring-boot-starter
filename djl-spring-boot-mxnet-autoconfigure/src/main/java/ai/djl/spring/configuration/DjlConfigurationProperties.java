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

    private ApplicationType applicationType;
    private Class<?> inputClass;
    private Class<?> outputClass;
    private EngineType engineType;
    private String artifactId;
    private Map<String, Object> arguments;

    /**
     * Criteria used as filters for model lookup.
     * For more information on available criteria that are currently part of the repository see
     * https://github.com/awslabs/djl/tree/master/mxnet/mxnet-model-zoo
     */
    private Map<String, String> modelFilter;

    public Map<String, String> getModelFilter() {
        return modelFilter;
    }

    public void setModelFilter(Map<String, String> modelFilter) {
        this.modelFilter = modelFilter;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public Class<?> getInputClass() {
        return inputClass;
    }

    public void setInputClass(Class<?> inputClass) {
        this.inputClass = inputClass;
    }

    public Class<?> getOutputClass() {
        return outputClass;
    }

    public void setOutputClass(Class<?> outputClass) {
        this.outputClass = outputClass;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }
}
