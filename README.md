# README #

This README would normally document whatever steps are necessary to get your application up and running.

## First steps

#### Prerequisites ####
- **Java 8 JDK**

##### For the designer-frontend submodule #####
- **NPM** - Node package manager, can be installed by installing [NodeJS LTS](http://nodejs.org)
- **Angular-cli**, can be installed with npm ```npm install -g @angular/cli```

## Summary of set up ##
The ESPD/VCD Project consists of two main sub components and several auxiliary that are part of the subcomponents:
1. The **ESPD/VCD Framework**, which consists of the ESPD/VCD **Builder**, **Model**, **Validator**, **EDM** (which contains the schema classes) and **Codelists**.
2. The **ESPD/VCD System**, which consists of the ESPD/VCD **Designer web application** which also uses the ESPD/VCD Framework. 

The project uses **gradle** for dependency management, build, testing and running **except for the designer-frontend** module which uses **npm** and the **angular-CLI**.
The initial setup of the gradle environment can be created by executing the the `wrapper` task:

`$ gradlew wrapper`

## Configuration ##
The **ESPD designer** has two parts: 
- **Designer-backend** which contains a Spark Web server that hosts a REST API for the ESPD/VCD Framework
- **Designer-frontend** which contains the Angular web application that consumes the REST API and provides a user-friendly interface for working with ESPDs

## Compilation ##
1. ```$ gradlew build clean```
    This will build all the sub projects' jar-files. Tests, if available should be run by each sub project. The clean task will clean up all the build files.
2. ```$ ng build --prod```
    If run in the designer-frontend modules' folder, this will build the Angular web application.
    This will produce a `dist` folder that contains the self-contained angular web application ready for deployment in a web server.
3. ```$ gradlew :designer:designer-backend:jar```
    This will create a (fat) jar in the `designer/designer-backend/build/libs/` folder that contains the designer-backend and all of its dependencies, including the ESPD/VCD framework. 
    To start the server from the jar file, simply run:
    ```$ java -jar designer-backend-2.0.2.jar 8080``` 
    8080 being the port that the server will listen to. If you do not specify a port argument, it will default to 8080.

## How to run the designer ##
The actual deployable artifact is the ESPD/VCD Designer, found in the designer submodule. 

##### Designer-backend #####
A quick deployment for testing can be achieved through gradle:

``` $ gradlew run```  

If all goes well, the designer-backend REST API server should be up and running at <http://localhost:8080/api>

##### Designer-frontend #####
A quick deployment for testing can be achieved though the angular-CLI. From the designer-frontend directory, run the following:

1. Before running the server, install the dependencies by using:
```$ npm install```

2. Then use the angular-cli to start a live development server:
```$ ng serve```

The designer-frontend angular UI should now be accessible at <http://localhost:4200>

For more information about the *designer-frontend* submodule, please read its README.MD file.