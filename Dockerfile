# Dockerfile simples para deploy no render

# Usa imagem Maven para compilar o projeto
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Define pasta de trabalho dentro do container
WORKDIR /app

# Copia todo o código para dentro do container
COPY . .

# Compila e gera o JAR (pula testes)
RUN mvn clean package -DskipTests

# Apenas o Java Runtime
FROM eclipse-temurin:21-jre

# Define pasta de trabalho dentro do container
WORKDIR /app

# Copia o JAR na etapa anterior
COPY --from=build /app/target/*.jar app.jar


# Informa que a aplicação usa a porta 8080
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT [ "java", "-jar", "app.jar" ]
