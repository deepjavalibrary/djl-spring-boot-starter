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

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
@Configuration
@ConditionalOnMissingBean(ZooModel.class)
@EnableConfigurationProperties(DjlConfigurationProperties.class)
public class DjlAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(DjlAutoConfiguration.class);

    @Autowired(required = false)
    private Translator translator;

    @Autowired
    private DjlConfigurationProperties properties;

    /**
     * Image factory is a convenience mechanism to create images from various resources like input streams, files, etc.
     * that could be then used with the predictor for inference.
     *
     * @return instance of the {@link ImageFactory}
     */
    @Bean
    public ImageFactory imageFactory() {
        return ImageFactory.getInstance();
    }

    @Bean
    public ZooModel<?, ?> model() throws MalformedModelException, ModelNotFoundException, IOException {
        var applicationType = properties.getApplicationType();
        var filter = properties.getModelFilter();
        var arguments = properties.getArguments();
        var artifactId = properties.getModelArtifactId();
        var inputClass = properties.getInputClass();
        var urls = properties.getUrls();

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
        if(translator != null) {
            builder.optTranslator(translator);
        }

        if(urls != null && urls.length > 0) {
            builder.optModelUrls(StringUtils.arrayToCommaDelimitedString(urls));
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
        if (translator != null) {
            LOG.info("Applying custom translator {}", translator.getClass());
        }
        return () -> translator == null ? model.newPredictor() : model.newPredictor(translator);
    }
}
