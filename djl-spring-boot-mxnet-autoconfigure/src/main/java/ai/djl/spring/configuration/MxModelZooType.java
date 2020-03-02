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

import ai.djl.mxnet.zoo.MxModelZoo;
import ai.djl.repository.zoo.ModelLoader;

public enum MxModelZooType {
    MLP(MxModelZoo.MLP),
    SSD(MxModelZoo.SSD),
    RESNET(MxModelZoo.RESNET),
    RESNEXT(MxModelZoo.RESNEXT),
    SENET(MxModelZoo.SENET),
    SE_RESNEXT(MxModelZoo.SE_RESNEXT),
    SQUEEZENET(MxModelZoo.SQUEEZENET),
    SIMPLE_POSE(MxModelZoo.SIMPLE_POSE),
    MASK_RCNN(MxModelZoo.MASK_RCNN),
    ACTION_RECOGNITION(MxModelZoo.ACTION_RECOGNITION),
    BERT_QA(MxModelZoo.BERT_QA);

    private ModelLoader loader;

    MxModelZooType(ModelLoader loader) {
        this.loader = loader;
    }

    public ModelLoader getLoader() {
        return loader;
    }
}
