# --- Estágio 1: Build (JDK Completo) ---
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

# Cache de dependências eficiente
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
# Otimização: Baixa apenas o necessário para as dependências
RUN ./mvnw dependency:go-offline -B

# Compilação
COPY src ./src
RUN ./mvnw clean package -DskipTests

# --- Estágio 2: Execução (JRE Reduzido) ---
# Usamos a menor imagem oficial do JRE disponível
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Segurança: Rodar como usuário não-privilegiado diminui a superfície de ataque
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copia apenas o JAR (O pulo do gato: o nome fixo evita lixo de builds antigos)
COPY --from=build /app/target/*.jar app.jar

# Variáveis de ambiente para otimizar o uso de RAM em containers
ENV JAVA_OPTS="-XX:+UseParallelGC -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]