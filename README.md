# Inventory Service / 在庫管理サービス

Distributed Library System — Inventory Service  

分散型図書館システム — 在庫管理サービス

---

## Overview / 概要

The Inventory Service is an event-driven microservice within the distributed library platform.

It is responsible for maintaining book stock availability across the system.

The service consumes borrow and return events through Kafka, updates inventory state in real time, and exposes REST APIs for querying current availability.

It acts as the inventory source of truth for all circulation-related operations.

---

Inventory Service は分散型図書館システムにおける在庫管理を担当するイベント駆動型マイクロサービスです。

Kafka を通じて貸出・返却イベントを非同期に処理し、書籍ごとの在庫状態を更新します。

また、現在の貸出可能冊数を REST API 経由で提供し、システム全体における在庫の単一責任ソースとして機能します。

---

## Service Boundaries / サービス境界

### Responsibilities / 担当領域

- Borrow / return event consumption
- Inventory stock updates
- Availability state tracking
- Inventory query APIs
- Real-time stock consistency
- OpenAPI documentation generation

### Out of Scope / 対象外

- Catalog metadata management
- Borrowing history persistence
- User authentication / authorization
- Notification delivery
- Member lifecycle management

---

## Badges

[![Tests](https://github.com/damouu/library-app-inventory/actions/workflows/run-tests.yml/badge.svg?branch=test)](https://github.com/damouu/library-app-inventory/actions/workflows/run-tests.yml)

[![Merge PR](https://github.com/damouu/library-app-inventory/actions/workflows/merge-pr.yml/badge.svg)](https://github.com/damouu/library-app-inventory/actions/workflows/merge-pr.yml)

[![Prepare](https://github.com/damouu/library-app-inventory/actions/workflows/prepare.yml/badge.svg)](https://github.com/damouu/library-app-inventory/actions/workflows/prepare.yml)

[![Codecov](https://codecov.io/gh/damouu/library-app-inventory/branch/test/graph/badge.svg)](https://codecov.io/gh/damouu/library-app-inventory)

[![Git Tag](https://img.shields.io/github/v/tag/damouu/library-app-inventory?logo=github)](https://github.com/damouu/library-app-inventory/tags)

![Kafka](https://img.shields.io/badge/Kafka-integrated-orange)
![Prometheus](https://img.shields.io/badge/Prometheus-monitored-blue)

---

## Architecture Role / アーキテクチャ上の役割

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

## Technology Stack / 技術スタック

| Category | Technology |
|----------|------------|
| Runtime | Java 21 |
| Framework | Spring Boot 2.7 |
| API | Spring Web |
| Documentation | Springdoc OpenAPI |
| Messaging | Apache Kafka |
| Persistence | Spring Data JPA |
| Database | PostgreSQL / H2 |
| Validation | Bean Validation |
| Monitoring | Actuator / Micrometer / Prometheus |
| Testing | JUnit 5 / Mockito / Instancio / JaCoCo |
| CI/CD | GitHub Actions |

---

## API Surface / API概要

### Inventory Query Endpoints

Provides inventory availability information for:

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

## Local Development / ローカル開発

### Requirements

- Java 21
- Maven
- Docker
- PostgreSQL
- Kafka

### Run

```bash
docker compose up --build
```

---

## Testing / テスト

```bash
./mvnw verify
```

Includes:

- Unit tests (Surefire)
- Integration tests (Failsafe)
- Coverage validation
- Complexity enforcement

---

## Coverage Policy / カバレッジポリシー

This service enforces strict build quality gates.

### Global Rules

- Line coverage: **100%**
- Missed complexity: **0**

### Service Layer Rules

For `com.example.demo.service.*`

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

## Configuration / 設定

Environment variables:

```env
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
SPRING_KAFKA_BOOTSTRAP_SERVERS=
```

Environment-driven configuration is used for all runtime infrastructure.

---

## Observability / 可観測性

### Actuator

- `/actuator/health`
- `/actuator/metrics`
- `/actuator/prometheus`

### API Docs

- `/swagger-ui.html`
- `/v3/api-docs`

---

## Build Guarantees / 品質保証

The CI pipeline validates:

- Unit and integration test execution
- Coverage compliance
- Cyclomatic complexity thresholds
- Pull request verification
- Build reproducibility

---

## Future Improvements / 今後の改善

- Reservation / hold support
- Low-stock alerting
- WebSocket inventory updates
- Distributed tracing
- Event replay tooling

---

## License

MIT