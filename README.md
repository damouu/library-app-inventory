# Library App Inventory Service

Distributed Library System — Inventory Service

---

## Overview

The Inventory Service is an event-driven microservice within the distributed library platform.

It is responsible for maintaining book stock availability across the system.

The service consumes borrow and return events through Kafka, updates inventory state in real time, and exposes REST APIs for querying current availability.

It acts as the inventory source of truth for all circulation-related operations.

---

## Service Boundaries

### Responsibilities

- Borrow and return event consumption
- Inventory stock updates
- Availability state tracking
- Inventory query APIs
- Real-time stock consistency
- OpenAPI documentation generation

### Out of Scope

- Catalog metadata management
- Borrowing history persistence
- User authentication and authorization
- Notification delivery
- Member lifecycle management

---

## Badges

[![Tests](https://github.com/damouu/library-app-inventory/actions/workflows/run-tests.yml/badge.svg?branch=test)](https://github.com/damouu/library-app-inventory/actions/workflows/run-tests.yml) [![Merge PR](https://github.com/damouu/library-app-inventory/actions/workflows/merge-pr.yml/badge.svg)](https://github.com/damouu/library-app-inventory/actions/workflows/merge-pr.yml) [![Prepare](https://github.com/damouu/library-app-inventory/actions/workflows/prepare.yml/badge.svg)](https://github.com/damouu/library-app-inventory/actions/workflows/prepare.yml) [![Codecov](https://codecov.io/gh/damouu/library-app-inventory/branch/test/graph/badge.svg)](https://codecov.io/gh/damouu/library-app-inventory) [![Git Tag](https://img.shields.io/github/v/tag/damouu/library-app-inventory?logo=github)](https://github.com/damouu/library-app-inventory/tags)

[![Docker Image](https://img.shields.io/docker/v/damou/library-app-inventory?label=docker&logo=docker)](https://hub.docker.com/repository/docker/damou/library-app-inventory/general) [![Docker Pulls](https://img.shields.io/docker/pulls/damou/library-app-inventory?logo=docker)](https://hub.docker.com/repository/docker/damou/library-app-inventory/general) ![Kafka](https://img.shields.io/badge/Kafka-integrated-orange) ![Prometheus](https://img.shields.io/badge/Prometheus-monitored-blue)

**Docker Hub:** [damou/library-app-inventory](https://hub.docker.com/repository/docker/damou/library-app-inventory/general)

---

## Architecture Role

The Inventory Service sits between circulation events and availability queries.

It ensures stock changes are processed asynchronously while exposing low-latency read endpoints for frontend consumers.

### Event Flow

Borrow / Return Event

↓

Kafka Consumer

↓

Inventory Processing Layer

↓

Persistence Layer

↓

Availability State Updated

### Query Flow

Frontend Request

↓

REST Controller

↓

Service Layer

↓

Repository Access

↓

Availability Response

---

## Technology Stack

| Category      | Technology                             |
|---------------|----------------------------------------|
| Runtime       | Java 21                                |
| Framework     | Spring Boot 2.7                        |
| API           | Spring Web                             |
| Documentation | Springdoc OpenAPI                      |
| Messaging     | Apache Kafka                           |
| Persistence   | Spring Data JPA                        |
| Database      | PostgreSQL / H2                        |
| Validation    | Bean Validation                        |
| Monitoring    | Actuator / Micrometer / Prometheus     |
| Testing       | JUnit 5 / Mockito / Instancio / JaCoCo |
| CI/CD         | GitHub Actions                         |

---

## API Surface

### Inventory Query Endpoints

The service provides inventory availability information, including:

- Available copies
- Borrowable state
- Inventory consistency checks

---

## Example Response

```json
{
  "bookUuid": "123e4567-e89b-12d3-a456-426614174000",
  "availableCopies": 4,
  "totalCopies": 8,
  "borrowable": true
}
```

---

## Coverage Policy

This service enforces strict build quality gates.

### Global Rules

- Line coverage: **100%**
- Missed complexity: **0**

### Service Layer Rules

For `com.example.demo.service.*`:

- Complexity coverage: **100%**
- Total complexity ≤ **5**

### Coverage Exclusions

- DTOs
- Models
- Views
- Generated sources
- Configuration classes
- Bootstrap application class

---

## Configuration

The service uses environment-driven configuration.

```env
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
SPRING_KAFKA_BOOTSTRAP_SERVERS=
```

Environment-driven configuration is used for all runtime infrastructure.

---

## Observability

### Actuator

- `/actuator/health`
- `/actuator/metrics`
- `/actuator/prometheus`

### API Documentation

- `/swagger-ui.html`
- `/v3/api-docs`

---

## Build Guarantees

The CI pipeline validates:

- Unit and integration test execution
- Coverage compliance
- Cyclomatic complexity thresholds
- Pull request verification
- Build reproducibility

---

## Future Improvements

- Reservation and hold support
- Low-stock alerting
- WebSocket inventory updates
- Distributed tracing
- Event replay tooling

---

## License

MIT
