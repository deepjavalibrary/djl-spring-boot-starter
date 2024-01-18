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

import java.io.IOException;
import java.util.function.Supplier;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslatorFactory;
import ai.djl.util.ClassLoaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnMissingBean(ZooModel.class)
@EnableConfigurationProperties(DjlConfigurationProperties.class)
public class DjlAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(DjlAutoConfiguration.class);

    @Autowired
    private DjlConfigurationProperties properties;

    @Bean
    public ZooModel<?, ?> model() throws MalformedModelException, ModelNotFoundException, IOException {
        var applicationType = properties.getApplicationType();
        var filter = properties.getModelFilter();
        var arguments = properties.getArguments();
        var artifactId = properties.getModelArtifactId();
        var inputClass = properties.getInputClass();
        var urls = properties.getUrls();
        var translatorFactory = properties.getTranslatorFactory();

        if (inputClass == null) {
            LOG.warn("Input class is not defined. Using default: BufferedImage");
            inputClass = Image.class;
        }
        Class<?> outputClass = properties.getOutputClass();
        if (outputClass == null) {
            LOG.warn("Input class is not defined. Using default: DetectedObjects");
            outputClass = DetectedObjects.class;
        }

        Criteria.Builder<?, ?> builder = Criteria.builder().setTypes(inputClass, outputClass);
        if (applicationType != null) {
            builder.optApplication(applicationType.application());
        }
        if (filter != null) {
            builder.optFilters(filter);
        }
        if (artifactId != null) {
            builder.optArtifactId(artifactId);
        }
        if (arguments != null) {
            builder.optArguments(arguments);
        }
        if (translatorFactory != null) {
            ClassLoader cl = ClassLoaderUtils.getContextClassLoader();
            TranslatorFactory factory = ClassLoaderUtils.initClass(cl, TranslatorFactory.class, translatorFactory);
            builder.optTranslatorFactory(factory);
        }
        if(urls != null && urls.length > 0) {
            builder.optModelUrls(StringUtils.arrayToCommaDelimitedString(urls));
        }
        if (properties.isProgress()) {
            builder.optProgress(new ProgressBar());
        }
        if (StringUtils.hasText(properties.getModelName())) {
            builder.optModelName(properties.getModelName());
        }
        if (StringUtils.hasText(properties.getGroupId())) {
            builder.optGroupId(properties.getGroupId());
        }
        if (StringUtils.hasText(properties.getEngine())) {
            builder.optEngine(properties.getEngine());
        }

        try {
            var zooModel = builder.build().loadModel();
            LOG.info("Successfully loaded model {}", zooModel.getName());
            return zooModel;
        }
        catch(ModelNotFoundException ex) {
            Yaml yaml = createYamlDumper();
            LOG.error("Requested model was not found");
            LOG.error("List of available models {}", yaml.dump(ModelZoo.listModels()));
            throw ex;
        }
    }

    private Yaml createYamlDumper() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        return new Yaml(options);
    }

    /**
     * Expected to be used with try-with-resources. The provided predictor is {@link AutoCloseable}.
     *
     * @param model injected configured model
     * @return provider of the predictor object
     */
    @Bean
    public Supplier<Predictor<?, ?>> predictorProvider(ZooModel<?, ?> model) {
        return model::newPredictor;
    }
}
