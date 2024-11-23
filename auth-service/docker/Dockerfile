FROM openjdk:8-jdk-alpine
EXPOSE 8889
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} auth-service-1.0.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","auth-service-1.0.0-SNAPSHOT.jar"]