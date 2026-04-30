# Projet DevOps - LSI 1

Application **Spring Boot** de démonstration autour d’un parc automobile et d’un historique de locations, avec persistance **PostgreSQL**, migrations **Flyway**, conteneurisation et chaîne **CI** (tests, couverture, analyse de la qualité du code, image Docker).

---

## Auteurs

- **Alexandre Borny**
- **Rémi Desjardins**

---

## Prérequis

| Outil | Version cible |
|-------|----------------|
| Java | **21** (toolchain Gradle) |
| Docker | pour `docker compose`, Testcontainers et le job image CI |

---

## Stack technique

- **Spring Boot 3.4** (Web, Data JPA)
- **PostgreSQL 16** (schéma versionné par **Flyway** ; Hibernate en `validate`, sans génération automatique du schéma)
- **JUnit 5** + **Spring Boot Test** ; intégration avec **Testcontainers** (`jdbc:tc:postgresql:…`, profil `test`)
- **JaCoCo** (rapports XML/HTML pour Sonar et revue locale)
- **SonarCloud** (plugin Gradle, quality gate en CI lorsque le secret est configuré)
- **Gradle** (wrapper inclus)

---

## Architecture (aperçu)

| Couche | Rôle |
|--------|------|
| **Contrôleurs REST** | HTTP uniquement ; réponses et corps d’entrée basés sur des **DTO** (pas d’exposition directe des entités JPA). |
| **Services** | Règles métier et transactions (`@Transactional`). |
| **Repositories** | Accès données Spring Data JPA. |
| **Entités / Flyway** | Modèle objet aligné sur le schéma SQL (`V1__initial_schema.sql`). |

Règle métier notable en base : **une seule location active par véhicule** (index unique partiel sur `rental_records` où `status = 'ACTIVE'`), complété par des contrôles applicatifs et des exceptions HTTP (`@ResponseStatus` : 404, 409).

---

## API REST (résumé)

| Méthode | Chemin | Description |
|---------|--------|-------------|
| `GET` | `/` | Message de bienvenue (sanity check / health implicite dans l’image). |
| `POST` | `/cars` | Création d’un véhicule (corps JSON : `plateNumber`, `brand`, `price`). |
| `GET` | `/cars` | Liste des véhicules. |
| `GET` | `/cars/{plateNumber}` | Détail par plaque. |
| `DELETE` | `/cars/{plateNumber}` | Suppression. |
| `PUT` | `/cars/{plateNumber}/rent` | Marque le véhicule comme loué. |
| `PUT` | `/cars/{plateNumber}/return` | Retour de location côté parc. |
| `GET` | `/rentals` | Toutes les fiches de location. |
| `GET` | `/rentals/active` | Locations actives. |
| `GET` | `/rentals/customer/{customerName}` | Fiches par client. |
| `POST` | `/rentals/start` | Démarre une location (`plateNumber`, `customerName`, `dailyPrice` en query). |
| `PUT` | `/rentals/end/{plateNumber}` | Clôture la location active pour la plaque. |

Port HTTP par défaut : **8090** (`application.properties`).

---

## Exécution locale

### Base + application (recommandé)

À la racine du dépôt :

```bash
docker compose up -d --build
```

- **PostgreSQL** : port `5432` (utilisateur / base / mot de passe : `tpdevops`, selon `docker-compose.yml`).
- **Application** : [http://localhost:8090/](http://localhost:8090/)

Les variables `SPRING_DATASOURCE_*` sont injectées dans le service `app` pour cibler le conteneur `postgres`.

### Application seule (Gradle)

1. Démarrer une instance PostgreSQL compatible (même schéma / utilisateur que `application.properties` ou surcharger les variables d’environnement).
2. Lancer :

```bash
./gradlew bootRun
```

Flyway applique les migrations au démarrage.

---

## Tests et qualité

```bash
./gradlew test jacocoTestReport
```

- Les tests d’intégration héritent de `AbstractPostgresIT` : profil `test`, **pas** de remplacement auto de la datasource, **PostgreSQL** via Testcontainers.
- Rapport de couverture : `build/reports/jacoco/test/html/index.html`.

Analyse Sonar (ex. SonarCloud, secret `SONAR_TOKEN` requis) :

```bash
./gradlew sonar
```

En local sans attente de quality gate : `./gradlew sonar -Dsonar.qualitygate.wait=false`.

---

## Intégration continue (GitHub Actions)

Le workflow **CI** (`.github/workflows/action.yml`) enchaîne des jobs réutilisables :

1. **Build** (compilation, artefact Gradle)
2. **Tests** + JaCoCo (Docker disponible pour Testcontainers)
3. **Couverture** (consolidation / publication selon configuration)
4. **Sonar** (dépôts non fork, PR internes ; sinon job ignoré)
5. **Docker** (construction d’image si les étapes précédentes obligatoires sont vertes et Sonar réussi ou ignoré)

Les branches déclenchées : `main` et `develop` (push et pull request).

---

## Fichiers utiles

| Fichier | Rôle |
|---------|------|
| `docker-compose.yml` | Orchestration Postgres + image applicative. |
| `Dockerfile` | Image JRE 21 multi-étapes (build Gradle, runtime non-root, healthcheck sur `/`). |
| `src/main/resources/application.properties` | Datasource, JPA, Flyway, port. |
| `src/main/resources/db/migration/V1__initial_schema.sql` | Schéma initial PostgreSQL. |
| `build.gradle` | Dépendances, JaCoCo, Sonar, nom du JAR `Hello-0.0.1-SNAPSHOT.jar`. |
