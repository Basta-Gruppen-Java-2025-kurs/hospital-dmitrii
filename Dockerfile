FROM eclipse-temurin:21-jre-alpine
LABEL authors="Dimage"

WORKDIR /app

COPY target/*.jar app.jar
EXPOSE 8080/tcp

ENTRYPOINT ["java", "-jar", "app.jar"]