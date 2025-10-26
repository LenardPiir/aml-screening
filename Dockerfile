FROM openjdk:25-jdk-slim

WORKDIR /app

COPY . .

RUN ./gradlew bootJar

ENTRYPOINT ["java", "-jar", "build/libs/aml-screening-0.0.1-SNAPSHOT.jar"]