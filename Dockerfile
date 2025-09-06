FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Сначала скопируем pom.xml и подкачаем зависимости (кэшируется)
COPY pom.xml .
RUN mvn dependency:go-offline

# Теперь копируем исходники
COPY src src


# Собираем проект
RUN mvn clean package -DskipTests

# --- Runtime образ ---
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
