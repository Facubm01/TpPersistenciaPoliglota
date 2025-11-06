# Multi-stage build for Spring Boot (Java 17) sin wrapper (usa Maven oficial)

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Cache de dependencias
COPY pom.xml .
RUN mvn -q -B -DskipTests dependency:go-offline

# Compilar
COPY src src
RUN mvn -q -B -DskipTests package

# Runtime image
FROM eclipse-temurin:17-jre
ENV APP_HOME=/app
WORKDIR ${APP_HOME}

# Copiar jar construido
COPY --from=build /workspace/target/*-SNAPSHOT.jar app.jar

EXPOSE 8080

# JVM flags pueden sobreescribirse con JAVA_OPTS
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


