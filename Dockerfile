# --- Build stage ---
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Сначала только pom.xml, чтобы зависимости кешировались
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Теперь копируем исходники и собираем проект
COPY src src
RUN mvn clean package -DskipTests -B

# --- Runtime stage ---
FROM eclipse-temurin:21
WORKDIR /app

# Копируем собранный JAR из build stage
COPY --from=build /app/target/*.jar app.jar

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
