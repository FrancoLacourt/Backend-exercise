FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/springboot_exercise-0.0.1.jar
COPY ${JAR_FILE} app_springboot_exercise.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_springboot_exercise.jar"]