# Image applicative uniquement : la base de données n’est volontairement pas intégrée ici
# (séparation des rôles, mises à jour de Postgres indépendantes, 12‑factor). L’orchestration
# app + PostgreSQL se fait via docker compose (service « postgres » + service « app » sur le
# même réseau, URL JDBC hôte = nom du service).
#
# Démarrage : depuis la racine du dépôt, `docker compose up -d` (voyez docker-compose.yml).

# --- Build : mêmes versions qu’en local (Gradle Wrapper) pour reproductibilité.
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY gradle/wrapper gradle/wrapper
COPY gradlew build.gradle settings.gradle ./
RUN chmod +x gradlew
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# --- Runtime (JRE seulement, non-root, healthcheck HTTP sur l’endpoint /).
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S -G app app && apk add --no-cache wget
WORKDIR /app
COPY --from=build --chown=app:app /app/build/libs/Hello-0.0.1-SNAPSHOT.jar /app/app.jar
USER app
EXPOSE 8090
# Cibles par défaut quand l’appli rejoint le réseau « compose » (hôte = nom du service postgres).
# Surchargées par docker compose ou -e en docker run (ex. host.docker.internal:5432 depuis le conteneur).
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/tpdevops \
    SPRING_DATASOURCE_USERNAME=tpdevops \
    SPRING_DATASOURCE_PASSWORD=tpdevops
HEALTHCHECK --interval=30s --timeout=3s --start-period=25s --retries=3 \
  CMD wget -q -O- http://127.0.0.1:8090/ > /dev/null || exit 1
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-Djava.net.preferIPv4Stack=true", "-jar", "/app/app.jar"]
