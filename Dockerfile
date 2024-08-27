
FROM maven:3-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml ./pom.xml
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package

# Stage 2: Run
FROM tomcat:10.1-jdk17
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/api.war