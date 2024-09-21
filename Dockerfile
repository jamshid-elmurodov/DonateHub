FROM openjdk:17-jdk-slim

EXPOSE 8050

LABEL authors="jamshidelmurodov"

COPY /build/libs/DonateHub-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]