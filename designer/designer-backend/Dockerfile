FROM java:8-alpine
ARG baseHREF

WORKDIR /designer

COPY artifact-designer.jar /designer

EXPOSE 80

CMD ["java","-jar","artifact-designer.jar","8080", "$baseHREF"]