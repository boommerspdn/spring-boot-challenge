# User Management API – Spring Boot Challenge

A take-home challenge implementation: a lightweight REST API for user management built with Spring Boot 3.x and H2 (embedded database).

**For challenge requirements, see [CHALLENGE.md](CHALLENGE.md).**  
**For implementation details and trade-offs, see [SOLUTION.md](SOLUTION.md).**

---

## Quick Start

### Prerequisites
- JDK 21 or later
- No external database or additional setup required

### Run the service
```bash
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080`.

### Run tests
```bash
./mvnw test
```

### Run with Docker
```bash
docker compose up
```

---

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/users` | Create a new user |
| `GET` | `/api/users` | List all users (with optional pagination/filtering) |
| `GET` | `/api/users/{id}` | Retrieve a user by ID |
| `PUT` | `/api/users/{id}` | Update a user |
| `DELETE` | `/api/users/{id}` | Delete a user |

---

## Dependencies

| Dependency | Purpose |
|------------|---------|
| `spring-boot-starter-web` | REST API controllers and HTTP handling |
| `spring-boot-starter-data-jpa` | Database access and ORM |
| `spring-boot-starter-validation` | Input validation (JSR-380) |
| `h2` | Embedded in-memory database |
| `lombok` | Reduce boilerplate (getters, setters, constructors) |
| `jackson-databind` | JSON serialization/deserialization |
| `springdoc-openapi-starter-webmvc-ui` | Swagger UI documentation |
| `spring-boot-starter-test` | Unit and integration testing |

---

## Tools & Docs

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) (while running)
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (while running)
  - JDBC URL: `jdbc:h2:mem:userdb`
  - Username: `sa`
  - Password: _(blank)_

---

## Tooling

- **JDK**: 21
- **Spring Boot**: 3.5.x
- **Build**: Maven (wrapper included)
- **Database**: H2 (embedded, in-memory)

---

For full API specification and implementation notes, refer to [CHALLENGE.md](CHALLENGE.md) and [SOLUTION.md](SOLUTION.md).
