FROM openjdk:11-jre-slim

WORKDIR /designer

COPY artifact-designer.jar /designer
COPY application.properties.snf /designer/application.properties

EXPOSE 80

CMD ["java","-jar","artifact-designer.jar"]