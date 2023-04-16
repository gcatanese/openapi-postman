FROM openjdk:17-jdk-slim

RUN mkdir -p /software

ADD target/openapi-postman.jar /software/openapi-postman.jar

EXPOSE 8080

WORKDIR /software
CMD java -Dserver.port=$PORT $JAVA_OPTS -jar openapi-postman.jar



