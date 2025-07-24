# ====== Stage 1: Build ======
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy source files and build the project
COPY . .
RUN mvn clean package -DskipTests

# ====== Stage 2: Runtime ======
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar kakigowhere-backend.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "kakigowhere-backend.jar"]
