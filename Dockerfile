FROM openjdk:21-jdk

EXPOSE 8000

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]