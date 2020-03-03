# DJL Spring Boot Starter

## General

[DJL](https://github.com/awslabs/djl) stands for java deep learning library and as the name suggests is a library developed to help java developers to get started with deep learning.
This project is a spring-boot starter that allows spring boot developers to start using DJL for inference.

At present the starter only supports dependency management, however in the future it will also provide auto configurations for inference and training.

## Usage

Run the following command locally in order to install DJL spring boot starter in your local maven repository:

    ./mvnw install

Eventually, starter kit artifacts will be available in maven central, until then the above step is needed to make the dependencies available in the local manven repository. Similar, for gradle builds, `mavenLocal()` repository must be enabled. 

Dependencies are divided in two sections: starter dependencies (just the libraries) and spring auto-condiguration for mxnet. See below. 

### Dependency Starter  

The starter will configure dependencies supports either platform specific or automatic configuration.

Platform specific example that imports mxnet for OSX:

    <dependency>
        <groupId>ai.djl.spring</groupId>
        <artifactId>djl-spring-boot-starter-mxnet-osx-x86_64</artifactId>
        <version>${djl.starter.version}</version>
    </dependency>

Platform specific example that imports mxnet for Linux:

    <dependency>
        <groupId>ai.djl.spring</groupId>
        <artifactId>djl-spring-boot-starter-mxnet-linux-x86_64</artifactId>
        <version>${djl.starter.version}</version>
    </dependency>

Automatic configuration example:

Platform specific example that imports mxnet for OSX:

    <dependency>
        <groupId>ai.djl.spring</groupId>
        <artifactId>djl-spring-boot-starter-mxnet-auto</artifactId>
        <version>${djl.starter.version}</version>
    </dependency>

Note: with automatic configuration it is assumed that the target runtime environment has access to the internet since
the required platform dependency will be downloaded from the maven repository at runtime.  This may not be ideal for production environments. 

### Spring DJL MXNET Auto Configuration

The project also supplies spring auto configuration which simplies inference configuration by automatically
configuring model and predictor. 

To use Auto Configuration you will need to bring it as:
     
     <dependency>
          <groupId>ai.djl.spring</groupId>
          <artifactId>djl-spring-boot-starter-mxnet-autoconfigure</artifactId>
          <version>${djl.starter.version}</version>
     </dependency>
 
 Autoconfiguration supports properties that can help with automatic lookup of the model.
 For example (assuming `application.yml` is used):
 
     djl:
       mx-model-zoo-type: SSD
       model-criteria:
             backbone: resnet50
             size: 512
 
For more information on available criteria that are currently part of the repository see [this reference](https://github.com/awslabs/djl/tree/master/mxnet/mxnet-model-zoo).

## Examples

See `djl-spring-boot-console-sample`
[A more advanced example and demo of the starter capability is here.](https://github.com/awslabs/djl)

## License
This project is licensed under the Apache-2.0 License.

