FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/manga-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Xms2048m", "-Xmx6000m", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005", "-jar", "app.jar"]

EXPOSE 8080 5005