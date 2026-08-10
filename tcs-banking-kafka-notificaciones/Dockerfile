FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace
COPY tcs-banking-kafka-notificaciones/pom.xml tcs-banking-kafka-notificaciones/pom.xml
COPY tcs-banking-kafka-notificaciones/src tcs-banking-kafka-notificaciones/src
RUN mvn -B -ntp -f tcs-banking-kafka-notificaciones/pom.xml -DskipTests clean package

FROM eclipse-temurin:21-jre-jammy

RUN groupadd --system banking \
    && useradd --system --gid banking --home-dir /app banking

WORKDIR /app
COPY --from=build --chown=banking:banking \
    /workspace/tcs-banking-kafka-notificaciones/target/tcs-banking-kafka-notificaciones.jar app.jar

USER banking
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
