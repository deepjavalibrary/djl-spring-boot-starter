# DJL Spring Boot Starter

## General

DJL stands for java deep learning library and as the name suggests is a library developed.
This project is a spring-boot starter that allows spring boot developers to start using DJL for inference.

At present the starter only supports dependency management, however in the future it will also provide auto configurations for inference and training.

## Usage

Run the following command locally in order to install DJL spring boot starter in your local maven repository:

  ./mvnw install

Dependencies are divided in two sections:

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
 

## Examples

See `djl-spring-boot-console-sample`
<Reference to examples repo here>

## License
This project is licensed under the Apache-2.0 License.

