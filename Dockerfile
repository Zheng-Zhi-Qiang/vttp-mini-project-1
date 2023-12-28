FROM maven:3-eclipse-temurin-21 AS builder

WORKDIR /app

COPY mvnw .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

RUN mvn package -Dmaven.test.skip=true

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/vttp-mini-project-1-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080 SPRING_REDIS_HOST=localhost SPRING_REDIS_PORT=6379 SPRING_REDIS_USERNAME=NOT_SET SPRING_REDIS_PASSWORD=NOT_SET
ENV STOCK_API_KEY=NOT_SET

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar