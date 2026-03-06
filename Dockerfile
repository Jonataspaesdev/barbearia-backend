# ====== BUILD STAGE ======
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia primeiro o pom para aproveitar cache de dependências
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# Copia o código-fonte
COPY src ./src

# Gera o jar
RUN mvn -q -DskipTests clean package

# ====== RUN STAGE ======
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o jar gerado
COPY --from=build /app/target/*.jar app.jar

# Railway usa PORT por variável de ambiente; seu Spring já está preparado
EXPOSE 8080

# Inicia a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]