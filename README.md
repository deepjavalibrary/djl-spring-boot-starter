# DJL Spring Boot Starter

## General

The [Deep Java Library](https://github.com/awslabs/djl) (DJL) is a library developed to help Java developers get started with deep learning.
This project is a Spring Boot starter that allows Spring Boot developers to start using DJL for inference.

The starter currently only supports dependency management.

## Usage

Run the following command locally to install the DJL Spring Boot starter in your local maven repository:

    ./mvnw install

For gradle builds, the `mavenLocal()` repository must be enabled. 

Dependencies are divided into two sections: starter dependencies, which includes the libraries, and Spring autoconfiguration for Apache MXNet.

### Starter Dependencies

The starter configures dependencies using either platform-specific or automatic configuration.

#### Platform-specific

The following platform-specific example imports MXNet for OSX:

    <dependency>
        <groupId>ai.djl.spring</groupId>
        <artifactId>djl-spring-boot-starter-mxnet-osx-x86_64</artifactId>
        <version>${djl.starter.version}</version>
    </dependency>

The following platform-specific example imports MXNet for Linux:

    <dependency>
        <groupId>ai.djl.spring</groupId>
        <artifactId>djl-spring-boot-starter-mxnet-linux-x86_64</artifactId>
        <version>${djl.starter.version}</version>
    </dependency>


#### Automatic configuration

The following platform-specific example imports mxnet for OSX:

    <dependency>
        <groupId>ai.djl.spring</groupId>
        <artifactId>djl-spring-boot-starter-mxnet-auto</artifactId>
        <version>${djl.starter.version}</version>
    </dependency>

Note: with automatic configuration, the target runtime environment requires access to the internet because
the required platform dependency is downloaded from the maven repository at runtime.  This may not be ideal for production environments. 

### Spring DJL MXNet autoconfiguration

The project also provides Spring autoconfiguration, which completes inference configuration by automatically
configuring the model and predictor. 

To use autoconfiguration, use the following dependency:
     
     <dependency>
          <groupId>ai.djl.spring</groupId>
          <artifactId>djl-spring-boot-starter-mxnet-autoconfigure</artifactId>
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
        output-class: ai.djl.modality.cv.DetectedObjects
        # Define filters that matches your application's need
        model-filter:
            size: 512
            backbone: mobilenet1.0
        # Override default pre-processing/post-processing behavior
        arguments:
            threshold: 0.2
 
This configuration has IDE level support for content assistance in Intellij IDEA, Eclipse (with STS), and Netbeans.

For more information on available criteria that are currently part of the repository, see the [DJL - MXNet model zoo](https://github.com/awslabs/djl/tree/master/mxnet/mxnet-model-zoo).

## Examples

See `djl-spring-boot-console-sample`.
For a more advanced example of the starter's capability, see the [DJL Spring Boot Demo](https://github.com/awslabs/djl-spring-boot-starter-demo).

## License
This project is licensed under the Apache-2.0 License.

