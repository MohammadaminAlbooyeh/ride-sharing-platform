# Ride Sharing Platform

A ride-sharing platform backend built with **Spring Boot 3**, **PostgreSQL**, **Redis**, **Apache Kafka**, and **WebSockets**.

## Tech Stack

- **Java 21** / Spring Boot 3.3
- **Spring Data JPA** + **Flyway** (PostgreSQL)
- **Redis** GEO index for real-time driver geo lookup
- **Apache Kafka** for async ride/event messaging
- **WebSocket** for live location / ride-status streaming
- **Springdoc OpenAPI** for API documentation

## Modules (package `com.rsp`)

| Package          | Responsibility                                   |
|------------------|--------------------------------------------------|
| `config`         | Infrastructure beans (Redis, Kafka, WebSocket, Swagger) |
| `controller`     | REST endpoints                                    |
| `model`          | JPA entities, DTOs, enums                         |
| `repository`     | Spring Data repositories                          |
| `service`        | Business logic (rides, matching, pricing, tracking) |
| `geo`            | Geo utilities (distance, ETA, geo index)          |
| `matching`       | Driver matching strategies                        |
| `pricing`        | Fare calculation and surge pricing                |
| `statemachine`   | Ride state machine                                |
| `messaging`      | Kafka producers / consumers                       |
| `websocket`      | WebSocket handlers                                |
| `exception`      | Custom exceptions + global handler                |
| `util`           | Helpers (ride id generator)                       |

## Getting Started

### Prerequisites

- JDK 21
- Maven 3.9+
- Docker (for PostgreSQL, Redis, Kafka via `docker-compose`)

### Running with Docker

```bash
docker compose -f docker/docker-compose.yml up -d
```

### Running the application

```bash
mvn spring-boot:run
```

Default port: `8080`. Swagger UI is available at `http://localhost:8080/swagger-ui.html`.

### Running tests

```bash
mvn test
```

## Configuration

Environment-specific settings live in `src/main/resources/application.yml` and
`src/main/resources/application-dev.yml`. Database/Redis/Kafka connection values
are configurable via environment variables (see `application-dev.yml`).
