FROM openjdk:11-jre-slim

WORKDIR /designer

COPY artifact-designer.jar /designer
COPY config.properties.snf /designer/config.properties

EXPOSE 80

CMD ["java","-jar","artifact-designer.jar"]