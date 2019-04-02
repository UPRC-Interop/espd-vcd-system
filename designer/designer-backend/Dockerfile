FROM java:11-alpine

WORKDIR /designer

COPY artifact-designer.jar /designer
COPY config.properties.snf /designer/config.properties

EXPOSE 80

CMD ["java","-jar","artifact-designer.jar","8080"]