# Solution

## How to Run

### Prerequisites

- JDK 21 or later
- No external database or additional setup required

### Start the service

```bash
./mvnw spring-boot:run
```

The service starts at `http://localhost:8080`.

### Run the tests

```bash
./mvnw test
```

### H2 Console (optional)

Available at `http://localhost:8080/h2-console` while the service is running.

- JDBC URL: `jdbc:h2:mem:userdb`
- Username: `sa`
- Password: _(blank)_

---

## Tooling

| Tool | Version |
|------|---------|
| JDK | 21 |
| Spring Boot | 3.5.x |
| Build | Maven (wrapper included) |
| Database | H2 (embedded, in-memory) |

---

## Endpoints

| Method | Path | Success | Failure |
|--------|------|---------|---------|
| `POST` | `/api/users` | `201` with user body | `400` validation, `409` duplicate |
| `GET` | `/api/users` | `200` array | — |
| `GET` | `/api/users/{id}` | `200` user body | `404` not found, `400` non-numeric id |

All responses use the `ApiResponse<T>` envelope:

```json
{ "success": true, "data": { ... }, "error": null }
{ "success": false, "data": null, "error": { "status": 404, "message": "..." } }
```

---

## Assumptions and Trade-offs

- **Uniqueness checked at the service layer** (query-before-insert) rather than catching `DataIntegrityViolationException`. This gives a clearer, field-specific error message at the cost of a potential race condition under very high concurrency — acceptable for this scope.
- **`createdAt` is set server-side** via `@CreationTimestamp`; the client cannot supply or override it.
- **No seed data loaded on startup** — the database starts empty. Use `POST /api/users` or the H2 console to populate it.
- **Non-numeric `{id}`** returns `400` (not `500`) via `MethodArgumentTypeMismatchException` handled in the global `@ControllerAdvice`.
- **No authentication** — out of scope per the challenge spec.
