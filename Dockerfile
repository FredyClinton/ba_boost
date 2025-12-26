FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# On copie le pom.xml d'abord pour mettre les dépendances en cache
COPY pom.xml .
RUN mvn dependency:go-offline

# On copie le code et on compile
COPY src ./src
RUN mvn clean package -DskipTests

# Image finale d'exécution
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]