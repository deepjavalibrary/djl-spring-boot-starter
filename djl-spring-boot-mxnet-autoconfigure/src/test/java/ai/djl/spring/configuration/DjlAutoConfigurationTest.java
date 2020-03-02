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
import ai.djl.modality.cv.DetectedObjects;
import ai.djl.modality.cv.SingleShotDetectionTranslator;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.mxnet.zoo.MxModelZoo;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Pipeline;
import ai.djl.translate.Translator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DjlAutoConfigurationTest {

    private ApplicationContextRunner applicationContextRunner;

    @BeforeEach
    public void setUp() {
        applicationContextRunner = new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(DjlAutoConfiguration.class));
    }

    @Test
    public void defaultLoadConfiguration() {
        applicationContextRunner.run(context -> {
            assertThat(context).hasSingleBean(ZooModel.class);
            assertThat(context).hasBean("predictorProvider");
        });
    }

    @Test
    public void doesNotLoadConfigurationIfModelDefined() {
        applicationContextRunner.withUserConfiguration(SSDModelConfiguration.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(ZooModel.class);
                    assertThat(context).doesNotHaveBean("predictorProvider");
                });
    }

    @Test
    public void loadConfigurationWithPropertiesDefined() {
        applicationContextRunner.withPropertyValues(
                "djl.mxModelZooType = BERT_QA",
                "djl.modelCriteria.backbone = bert",
                "djl.modelCriteria.dataset = book_corpus_wiki_en_uncased")
                .run(context -> {
                    assertThat(context).hasSingleBean(ZooModel.class);
                    assertThat(context).hasBean("predictorProvider");
                });
    }

    @Test
    public void loadSSDWithCustomTranslator() {
        applicationContextRunner.withPropertyValues(
                "djl.mxModelZooType = SSD",
                "djl.modelCriteria.backbone = resnet50",
                "djl.modelCriteria.dataset = voc",
                "djl.modelCriteria.flavor = v1",
                "djl.modelCriteria.size = 512"
                )
                .withUserConfiguration(SSDWithTranslatorModelConfiguration.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(ZooModel.class);
                    assertThat(context).hasBean("predictorProvider");
                    assertThat(context.getBean("translator")).isInstanceOf(SingleShotDetectionTranslator.class);
                });
    }

    @Configuration
    static class SSDModelConfiguration {

        @Bean
        public Map<String, String> criteria() {
            Map<String, String> criteria = new HashMap<>();
            criteria.put("size", "512");
            criteria.put("backbone", "resnet50");
            criteria.put("flavor", "v1");
            criteria.put("dataset", "voc");
            return criteria;
        }

        @Bean
        public ZooModel<BufferedImage, DetectedObjects> model(@Qualifier("criteria") Map<String, String> criteria)
                throws MalformedModelException, ModelNotFoundException, IOException {
            return MxModelZoo.SSD.loadModel(criteria, new ProgressBar());
        }
    }

    /**
     * Configuration adds a custom translator.
     */
    @Configuration
    static class SSDWithTranslatorModelConfiguration {

        @Bean
        public Translator<BufferedImage, DetectedObjects> translator() {
            int width = 512;
            int height = 512;
            double threshold = 0.1d;

            Pipeline pipeline = new Pipeline();
            pipeline.add(new Resize(width, height)).add(new ToTensor());

            return SingleShotDetectionTranslator.builder()
                    .setPipeline(pipeline)
                    .setSynsetArtifactName("classes.txt")
                    .optThreshold((float) threshold)
                    .optRescaleSize(width, height)
                    .build();
        }
    }
}
