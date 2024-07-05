FROM openjdk:21-slim

COPY ./artistry-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-XX:+PrintGCDetails", "-XX:+PrintGCTimeStamps", "-Xloggc:gc.log", "-jar", "/app.jar", "--spring.profiles.active=dev"]