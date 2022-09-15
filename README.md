# DJL Spring Boot Starter

## General

The [Deep Java Library](https://github.com/deepjavalibrary/djl) (DJL) is a library developed to help Java developers get started with deep learning.
This project is a Spring Boot starter that allows Spring Boot developers to start using DJL for inference.

The starter supports dependency management and auto-configuration.

## Usage

The released artifacts are available from maven central. 

Unreleased artifacts (snapshots) can be downloaded using snapshot repository: https://oss.sonatype.org/conten/repositories/snapshots/.

Dependencies are divided into two sections: starter dependencies, which includes the libraries, and Spring autoconfiguration for Apache MXNet.

### Starter Dependencies

The starter configures dependencies using either platform-specific or automatic configuration.

#### MXNet Configuration

**MXNet for OSX**

    <dependency>
        <groupId>ai.djl.spring</groupId>
        <artifactId>djl-spring-boot-starter-mxnet-osx-x86_64</artifactId>
        <version>${djl.starter.version}</version>
    </dependency>

**MXNet for Linux**

    <dependency>
        <groupId>ai.djl.spring</groupId>
        <artifactId>djl-spring-boot-starter-mxnet-linux-x86_64</artifactId>
        <version>${djl.starter.version}</version>
    </dependency>


**MXNet Auto Configuration**

This will download the correct artifact at runtime (provided external internet egress is enabled), including Windows
 specific artifacts:

    <dependency>
        <groupId>ai.djl.spring</groupId>
        <artifactId>djl-spring-boot-starter-mxnet-auto</artifactId>
        <version>${djl.starter.version}</version>
    </dependency>

Note: with automatic configuration, the target runtime environment requires access to the internet because
the required platform dependency is downloaded from the maven repository at runtime.  This may not be ideal for production environments.

#### PyTorch Configuration

Auto configuration for PyTorch (the correct OS specific artifact is downloaded at runtime):

    <dependency>
        <groupId>ai.djl.spring</groupId>
        <artifactId>djl-spring-boot-starter-pytorch-auto</artifactId>
        <version>${djl.starter.version}</version>
    </dependency>

#### TensorFlow Configuration 

Auto configuration for TensorFlow (the correct OS specific artifact is downloaded at runtime):

    <dependency>
        <groupId>ai.djl.spring</groupId>
        <artifactId>djl-spring-boot-starter-tensorflow-auto</artifactId>
        <version>${djl.starter.version}</version>
    </dependency>

### Spring DJL MXNet Autoconfiguration

The project also provides Spring autoconfiguration, which completes inference configuration by automatically
configuring the model and predictor. 

To use autoconfiguration, use the following dependency:
     
     <dependency>
          <groupId>ai.djl.spring</groupId>
          <artifactId>djl-spring-boot-starter-autoconfigure</artifactId>
          <version>${djl.starter.version}</version>
     </dependency>
 
 Autoconfiguration supports properties that help with automatic lookup of the model.
 For example (assuming `application.yml` is used):
 
    djl:
        # Define application type
        application-type: OBJECT_DETECTION
        # Define input data type, a model may accept multiple input data type
        input-class: java.awt.image.BufferedImage
        # Define output data type, a model may generate different out put
        output-class: ai.djl.modality.cv.output.DetectedObjects
        # Define filters that matches your application's need
        model-filter:
            size: 512
            backbone: mobilenet1.0
        # Override default pre-processing/post-processing behavior
        arguments:
            threshold: 0.2
 
This configuration has IDE level support for content assistance in Intellij IDEA, Eclipse (with STS), and Netbeans.
For more information on available criteria that are currently part of the repository, see the [DJL - MXNet model zoo](https://github.com/deepjavalibrary/djl/tree/master/engines/mxnet/mxnet-model-zoo).

## Examples

See `djl-spring-boot-console-sample`.
For a more advanced example of the starter's capability, see the [DJL Spring Boot Demo](https://github.com/deepjavalibrary/djl-spring-boot-starter-demo).

The demo project contains instructions how to deploy DJL based microservices to [Amazon EKS (Elastic Kubernetes 
Service)](https://github.com/deepjavalibrary/djl-spring-boot-starter-demo#deploying-to-eks-amazon-elastic-kubernetes-service)
and [PCF (Pivotal Cloud Foundry)](https://github.com/deepjavalibrary/djl-spring-boot-starter-demo#deploying-to-pcf).

Also, please see the following post on the general applicability and usage: [Adopting machine learning in your
 microservices with DJL (Deep Java Library) and Spring Boot](https://aws.amazon.com/blogs/opensource/adopting-machine-learning-in-your-microservices-with-djl-deep-java-library-and-spring-boot/).

## License
This project is licensed under the Apache-2.0 License.

