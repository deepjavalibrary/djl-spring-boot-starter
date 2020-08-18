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

package ai.djl.spring.tensorflow;


import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.spring.configuration.DjlAutoConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.NestedRuntimeException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class DjlTensorFlowAutoConfigurationTest {

    private static final Logger LOG = LoggerFactory.getLogger(DjlTensorFlowAutoConfigurationTest.class);

    private ApplicationContextRunner applicationContextRunner;

    @BeforeEach
    public void setUp() {
        applicationContextRunner = new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(DjlAutoConfiguration.class));
    }

    @Test
    public void loadConfigurationWithPropertiesDefinedAndRunInference() {
        applicationContextRunner.withPropertyValues(
                "djl.application-type=IMAGE_CLASSIFICATION",
                "djl.model-artifact-id=mobilenet",
                "djl.input-class=ai.djl.modality.cv.Image",
                "djl.output-class=ai.djl.modality.Classifications")
                .run(context -> {
                    assertThat(context).hasSingleBean(ZooModel.class);
                    assertThat(context).hasBean("predictorProvider");
                    var predictor = (Supplier<Predictor<Image, Classifications>>) context.getBean("predictorProvider");
                    Classifications result = predictor.get().predict(getClassPathImage(("/puppy-in-white-and-red-polka.jpg")));
                    LOG.info(result.toString());
                });
    }

    private Image getClassPathImage(String uri) throws IOException {
        return ImageFactory.getInstance().fromInputStream(this.getClass().getResourceAsStream(uri));
    }

}
