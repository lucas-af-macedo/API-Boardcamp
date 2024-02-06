# Estágio de construção
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .

# Estágio de execução
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar api.jar
EXPOSE 8080
CMD ["java", "-jar", "api.jar"]