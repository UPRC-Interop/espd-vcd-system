FROM java:8-alpine

WORKDIR /designer

COPY artifact-designer.jar /designer

EXPOSE 80

CMD ["java","-jar","artifact-designer.jar","8080", "/nightly_dev"]