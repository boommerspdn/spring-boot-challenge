# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Challenge Scope

This is a **User Management API** take-home challenge. Implement exactly what CHALLENGE.md specifies — no more, no less. **Always ask the user before handling any edge case not explicitly covered in CHALLENGE.md.**

## Build & Run

```bash
# Run the application
./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=UserServiceTest

# Package
./mvnw package -DskipTests
```

H2 console is available at `http://localhost:8080/h2-console` once the app is running.

## Project Configuration

- **Java:** 21
- **Spring Boot:** 3.x
- **Build:** Maven (wrapper included)
- **Database:** H2 embedded (auto-configured, no external setup)
- **Base package:** `com.sapondanai.spring_boot_challenge`
- **Config file:** `src/main/resources/application.yaml` — all datasource, port, and H2 console settings belong here

## Required API

| Method | Path | Success | Failure |
|--------|------|---------|---------|
| `POST` | `/api/users` | `201` with user body | `400` (validation), `409` (duplicate username/email) |
| `GET` | `/api/users` | `200` array (empty array if none) | — |
| `GET` | `/api/users/{id}` | `200` with user body | `404` with error body; no `500` on non-numeric id |

**Minimum user fields:** `id`, `username`, `email`, `firstName`, `lastName`, `createdAt`

All `/api/**` responses must use `Content-Type: application/json`.

## Layered Architecture

Implement exactly three layers with clear separation:

```
com.sapondanai.spring_boot_challenge/
├── controller/   # HTTP request/response only; delegates to service
├── service/      # Business logic, validation rules, uniqueness checks
├── repository/   # JPA interfaces (extend JpaRepository)
├── entity/       # JPA @Entity classes mapped to H2 tables
├── dto/          # Request/response shapes (keep entity off the wire)
└── exception/    # Custom exceptions + @ControllerAdvice global handler
```

## ApiResponse Shape

**Every** endpoint wraps its payload in a single envelope DTO. Define one generic `ApiResponse<T>` class and use it everywhere — both success and error paths.

```json
// Success
{
  "success": true,
  "data": { "id": 1, "username": "jsmith", ... }
}

// Error
{
  "success": false,
  "error": { "status": 404, "message": "User not found" }
}
```

- `success: true` → `data` is populated, `error` is null
- `success: false` → `error` is populated, `data` is null
- The global `@ControllerAdvice` handler must return `ApiResponse` (not raw strings or maps).

## Key Implementation Notes

- Use `@Valid` on controller method parameters + JSR-380 annotations (`@NotBlank`, `@Email`) on request DTOs for field validation.
- Enforce `username` and `email` uniqueness at the service layer (catch `DataIntegrityViolationException` or query before insert — pick one and be consistent).
- Handle non-numeric `{id}` path variable without a 500: either catch `MethodArgumentTypeMismatchException` in the global handler or use `Long` with Spring's default binding error handling.
- The global exception handler (`@ControllerAdvice`) is the single place to convert exceptions to HTTP responses.
- `createdAt` should be set server-side (e.g., `@CreationTimestamp` or set in service), never from client input.
