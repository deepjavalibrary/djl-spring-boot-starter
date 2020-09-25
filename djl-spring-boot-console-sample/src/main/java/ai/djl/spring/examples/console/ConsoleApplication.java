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
package ai.djl.spring.examples.console;

import ai.djl.inference.Predictor;

import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;

import ai.djl.translate.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

@SpringBootApplication
public class ConsoleApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ConsoleApplication.class);

    private static final Comparator<Classifications.Classification> byProbability =
            (Classifications.Classification o1, Classifications.Classification o2)->
                    Double.valueOf(o2.getProbability()).compareTo(o1.getProbability());

    /**
     * Note: @{@link Autowired} will fail on matching the generic type here.
     * To wire with Autowired and generic types consider the following:
     * <code>
     *     @Autowired
     *     Supplier<Predictor <?, ?>> autowiredProvider;
     * </code>
     *
     * Then casting to the right type.
     */
    @Resource
    private Supplier<Predictor<Image, DetectedObjects>> predictorProvider;

    // @SuppressWarnings("rawtypes")
    // @Bean
    // public Translator translator() {
    //     return ImageClassificationTranslator.builder()
    //             .addTransform(new ToTensor())
    //             .optFlag(Image.Flag.GRAYSCALE)
    //             .optApplySoftmax(true)
    //             .build();
    // }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        var resources = new org.springframework.core.io.Resource[] {
                new ClassPathResource("/puppy-in-white-and-red-polka.jpg"),
                new ClassPathResource("/street-car-bus-truck.jpg"),
                new ClassPathResource("/various-objects.png")
        };

        for(var resource : resources) {
            runInference(resource);
        }
    }

    private void runInference(org.springframework.core.io.Resource resource) throws IOException, ai.djl.translate.TranslateException {
        Image image = ImageFactory.getInstance().fromInputStream(resource.getInputStream());

        try (var predictor = predictorProvider.get()) {
            var results = predictor.predict(image);

            var items = results.items();
            items.sort(byProbability);
            if(items.size() > 10) {
                items = items.subList(0, 9);
            }
            for(var result : items) {
                LOG.info("results for {}: {}", resource.getFilename(), result.toString());
            }
           saveBoundingBoxImage(image, results, resource.getFilename());
        }
    }


    private static void saveBoundingBoxImage(Image img, DetectedObjects detection, String fileName)
            throws IOException {
        Path outputDir = Paths.get("target/output");
        Files.createDirectories(outputDir);

        // Make image copy with alpha channel because original image was jpg
        Image newImage = img.duplicate(Image.Type.TYPE_INT_ARGB);
        newImage.drawBoundingBoxes(detection);

        Path imagePath = outputDir.resolve(fileName);

        // OpenJDK can't save jpg with alpha channel
        newImage.save(Files.newOutputStream(imagePath), "png");
        LOG.info("Detected objects image has been saved in: {}", imagePath);
    }
}
