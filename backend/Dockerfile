FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/*.jar
COPY target/springboot_exercise-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]