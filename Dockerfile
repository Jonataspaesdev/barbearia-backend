# ====== BUILD STAGE ======
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia arquivos do Maven primeiro (melhora cache)
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# Copia o código e gera o jar
COPY src ./src
RUN mvn -q -DskipTests clean package

# ====== RUN STAGE ======
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o jar gerado
COPY --from=build /app/target/*.jar app.jar

# Render define PORT automaticamente; seu Spring já lê PORT
EXPOSE 8080

# Inicia a aplicação
CMD ["java", "-jar", "app.jar"]
