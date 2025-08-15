# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copy only the pom.xml first and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Now copy only the source files
COPY src ./src

# 3. Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE ${APP_PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]
