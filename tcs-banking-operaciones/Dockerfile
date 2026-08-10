FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY tcs-banking-core/pom.xml tcs-banking-core/pom.xml
COPY tcs-banking-core/src tcs-banking-core/src
RUN mvn -B -ntp -f tcs-banking-core/pom.xml -DskipTests clean install

COPY tcs-banking-operaciones/pom.xml tcs-banking-operaciones/pom.xml
COPY tcs-banking-operaciones/src tcs-banking-operaciones/src
RUN mvn -B -ntp -f tcs-banking-operaciones/pom.xml -DskipTests clean package

FROM eclipse-temurin:21-jre-jammy

RUN groupadd --system banking \
    && useradd --system --gid banking --home-dir /app banking

WORKDIR /app
COPY --from=build --chown=banking:banking \
    /workspace/tcs-banking-operaciones/target/tcs-banking-operaciones.jar app.jar

USER banking
EXPOSE 9091

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
