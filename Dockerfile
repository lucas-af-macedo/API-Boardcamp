# Estágio de construção
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .

RUN apt-get update \
 && apt-get install -y git vim curl build-essential wget tmux\
      # for vim extensions
      exuberant-ctags libcurl4-openssl-dev \
 && apt-get clean

ADD https://raw.githubusercontent.com/begriffs/haskell-vim-now/master/install.sh /install.sh
RUN chmod +x /install.sh
RUN /install.sh && rm -r /root/.cabal

RUN ./mvnw clean install -DskipTests

# Estágio de execução
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar api.jar
EXPOSE 8080
CMD ["java", "-jar", "api.jar"]