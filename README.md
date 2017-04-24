# README #

This README would normally document whatever steps are necessary to get your application up and running.

## Summary of set up ##
The ESPD/VCD Project consists of two main sub components and several auxiliary that are part of the subcomponents:
1. The ESPD/VCD Framework, which consists of the ESPD/VCD Builder and the Sch
2. The ESPD/VCD System, which consists of the ESPD/VCD Designer web application which also uses the ESPD/VCD Framework. 

The project uses gradle for dependency managment, build, testing and running.
The inital setup of the gradle enviroment can be created by executing the the `wrapper` task:

`$ gradlew wrapper`

## Configuration ##
The VCD System is a self contained Web Application with minimum external dependences. The only requirement is a servlet 3.0 web application server like tomcat 8. The deployment of the webapp is a simple WAR deployment in the servlet 3.0 application server.
## Compilation ##
1. ```$ gradlew build clean```
    This will build all the sub projects' jar and war-files. Tests, if available should be run by each sub project. The clean task will clean up all the build files.
   
## How to run tests ##
## Deployment instructions ##
The actual deployable artifact is the ESPD/VCD Designer, found in the designer submodule. A quick deployment for testing can be achived through gradle:

``` $ gradlew clean appRun```  

If all goes well, a tomcat instance should be up and running the VCD Designer Application at http://localhost:8080
