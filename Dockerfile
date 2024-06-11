FROM openjdk:21-slim

COPY ./artistry-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=dev"]