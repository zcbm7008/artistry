FROM openjdk:21-slim

COPY ./artistry-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Xms2048m", "-Xmx2048m", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-Xlog:gc*:gc.log", "-jar", "/app.jar", "--spring.profiles.active=dev"]