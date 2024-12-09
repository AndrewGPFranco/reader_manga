FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/manga-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005", "-jar", "app.jar"]

EXPOSE 8080
EXPOSE 5005