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

import ai.djl.Application;
import ai.djl.Application.CV;
import ai.djl.Application.NLP;

public enum ApplicationType {
    QUESTION_ANSWER(NLP.QUESTION_ANSWER),
    TEXT_CLASSIFICATION(NLP.TEXT_CLASSIFICATION),
    IMAGE_CLASSIFICATION(CV.IMAGE_CLASSIFICATION),
    OBJECT_DETECTION(CV.OBJECT_DETECTION),
    ACTION_RECOGNITION(CV.ACTION_RECOGNITION),
    INSTANCE_SEGMENTATION(CV.INSTANCE_SEGMENTATION),
    POSE_ESTIMATION(CV.POSE_ESTIMATION),
    SEMANTIC_SEGMENTATION(CV.SEMANTIC_SEGMENTATION);

    private Application application;

    ApplicationType(Application application) {
        this.application = application;
    }

    Application application() {
        return application;
    }
}
