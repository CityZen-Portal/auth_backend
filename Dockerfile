## Use an official JDK 17 runtime as a parent image
#FROM openjdk:17-jdk-slim
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the JAR file into the container
#COPY target/*.jar app.jar
#
## Expose port 8080 or the one your Spring Boot app uses
#EXPOSE 18010
#
## Run the JAR file
#ENTRYPOINT ["java", "-jar", "app.jar"]
#
### Set environment variables
##ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
##    JAVA_OPTS=""
#
### Add a volume pointing to /tmp
##VOLUME /tmp

FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]