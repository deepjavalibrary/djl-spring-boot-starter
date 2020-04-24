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
     * Defines the type of application for the model.
     *
     * <p>
     * For more information on available applications, see
     * https://github.com/awslabs/djl/blob/master/api/src/main/java/ai/djl/Application.java
     */
    private ApplicationType applicationType;

    /**
     * Defines the java data type used as input for inference. For example, {@link java.awt.image.BufferedImage} can be used for cases when input is image.
     */
    private Class<?> inputClass;

    /**
     * Defines the java data type expected for inference output. {@link ai.djl.modality.cv.output.DetectedObjects} is a common output for classifications, object detection.
     */
    private Class<?> outputClass;

    /**
     * Defines the artifactId of the model to be loaded.
     */
    private String modelArtifactId;

    /**
     * Arguments that allow the user to override pre-process/post-process behavior.
     *
     * <p>
     * The key/value pairs are model specific, check specific (@code ModelLoader} class for detail.
     *
     */
    private Map<String, Object> arguments;

    /**
     * Filters used to lookup a model from model zoo.
     *
     * <p>
     * For more information on available filters that are currently part of the repository, see
     * https://github.com/awslabs/djl/tree/master/model-zoo#how-to-find-a-pre-trained-model-in-the-model-zoo
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

    public String getModelArtifactId() {
        return modelArtifactId;
    }

    public void setModelArtifactId(String modelArtifactId) {
        this.modelArtifactId = modelArtifactId;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }
}
