FROM maven:3.9-eclipse-temurin-17 AS builder
RUN mkdir /app
WORKDIR /app
COPY pom.xml .
COPY src src
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests

FROM eclipse-temurin:17-jdk
RUN apt-get update
RUN addgroup appgroup && adduser --ingroup appgroup --disabled-password --gecos "" appuser
USER appuser
COPY --from=builder /app/target/customer-app.jar customer-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","customer-app.jar"]