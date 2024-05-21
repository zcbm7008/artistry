FROM openjdk:11-jre-slim

VOLUME /tmp

EXPOSE 8080

COPY target/artistry-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]