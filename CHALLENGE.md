# Java Spring Boot take-home challenge

This challenge is the **first screening step** for backend software engineers who will work on **real, production-style** Spring Boot services. We use it to see whether you have **practical familiarity** with the platform—REST API design, data modeling, input validation, error handling, and code you could maintain on a real team—not only whether a submission compiles.

**Time expectation:** **1–3 hours**, depending on your experience and tooling. The scope below is intentionally small; use any time left for polish or the bonus criteria.

**What you submit:** A **complete, runnable** Spring Boot application (see [Deliverables](#deliverables)). This repository intentionally contains **only a minimal starter project**—there is **no entity, controller, or service implementation** here. The design is yours.

### Use of AI coding assistants

**Generative AI tools (e.g. ChatGPT, Copilot, Cursor) are allowed** for this take-home. We do not require you to avoid them.

This exercise is still meant to reflect **your** baseline understanding of Spring Boot and **how you work on real services**—wiring controllers, validating input, structuring layers, and handling failure gracefully. **We strongly recommend doing the core work yourself** (with AI limited to small questions, docs lookup, or refactors you fully understand). If the result looks like something you cannot walk through or change confidently, that usually shows up quickly in review and in a follow-up conversation, and it works against the goal of the screen.

If you do use AI heavily, treat the output as **your** code: read it, run it, cover edge cases, and be prepared to explain **why** key parts are written the way they are.

---

## Scenario

Build a small **User Management API**: create users, retrieve a list, and look up a single user by ID. The service will act as a backend for a frontend team building a customer portal—it must be easy to run locally and straightforward to call from another application.

Use an **embedded H2 database** so reviewers can clone and run immediately without any external setup.

**Base URL:** `http://localhost:8080`

Required endpoints:

| Action | Method | Path | Notes |
|--------|--------|------|-------|
| Create user | `POST` | `/api/users` | Validate required fields; enforce unique `username` and `email`. |
| List users | `GET` | `/api/users` | Return all users as a JSON array. |
| Get user | `GET` | `/api/users/{id}` | Return a single user; 404 if not found. |

**Expected user shape** (minimum fields—you may add more):

```json
{
  "id": 1,
  "username": "jsmith",
  "email": "john.smith@example.com",
  "firstName": "John",
  "lastName": "Smith",
  "createdAt": "2024-01-15T09:23:00Z"
}
```

A `data/users.json` file is included in the starter for reference—it shows the expected shape and can be used as seed data if you choose.

---

## Functional requirements

### 1. Create user — `POST /api/users`

- Accept a JSON request body with at minimum: `username`, `email`, `firstName`, `lastName`.
- **Validate** all required fields are present and non-blank; return a `400` with a meaningful error body when validation fails.
- **Enforce uniqueness** on `username` and `email`; return a `409` (or equivalent meaningful error) if either is already taken.
- On success, return the created user with a `201` status.

### 2. List users — `GET /api/users`

- Return all users as a JSON array.
- Return an empty array (not an error) when no users exist.

### 3. Get user by ID — `GET /api/users/{id}`

- Return the matching user as a JSON object.
- Return a `404` with a meaningful error body when no user with that ID exists.
- Handle a non-numeric `id` gracefully (do not return a 500).

### 4. Project and runtime expectations

- The service must start with **`./mvnw spring-boot:run`** (or `./gradlew bootRun` if you use Gradle) with no extra setup steps beyond a standard JDK.
- Use **Java 21 or later** and **Spring Boot 3.x**.
- Use an **embedded database** (H2 is preconfigured in the starter)—reviewers should not need to install or start a database.
- All **API endpoints** for this challenge (for example, routes under **`/api/...`**) must return **`application/json`**.
- In your submission README, note the **JDK version** you built and tested with.

---

## Non-functional expectations (keep these lightweight)

- **Readable layering**: controller, service, and repository concerns should be easy to distinguish (exact package structure is up to you).
- **One place for configuration**: base URL, port, datasource settings in `application.properties`—not scattered through the code.
- **Consistent error responses**: all error responses should follow the same JSON shape so a frontend can handle them predictably.
- **Optional polish** (nice if you have time): request/response logging, a global exception handler (`@ControllerAdvice`), or meaningful field-level validation messages.

---

## Out of scope (do not spend time here)

- Authentication or authorization.
- Frontend code or UI.
- Deployment to a remote environment.
- Pixel-perfect API design matching a specification document—sensible, consistent REST conventions are enough.

---

## Deliverables

1. **Repository or archive** containing:
   - Full source for a **runnable** Spring Boot application (`pom.xml` or `build.gradle`, source files, `application.properties`).
   - A **candidate `SOLUTION.md`** at the project root with:
     - How to install and run the service (step-by-step).
     - The **JDK version** and any other tooling you used.
     - Any assumptions or trade-offs you made (short bullet list).
2. Submission method: follow instructions from the hiring team (e.g. **private GitHub invite**, **zip**, or **`git format-patch`**—use whatever they specify).

**Important:** We expect a **working service**, not a partial scaffold. If something is unfinished, say so in your `SOLUTION.md`; missing **create**, **list**, or **detail** endpoints will affect scoring.

---

## Allowed tooling

- **Maven** or **Gradle**—both are acceptable (Maven starter is provided).
- Any **Spring Boot** libraries or third-party dependencies you find appropriate. Avoid unnecessary ones; be prepared to justify choices in review.
- **Lombok** is included in the starter and may be used freely.

---

## Getting started

A minimal starter project is included. Dependencies are wired up; implementation is not.

```bash
cd challenge-java

# Start the application (downloads Maven on first run if needed)
./mvnw spring-boot:run
```

The starter includes:
- Spring Web, Spring Data JPA, H2, Spring Validation, and Lombok dependencies
- A bare `ChallengeApplication.java` main class
- `application.properties` pre-configured for H2 (console available at `/h2-console`)
- `data/users.json` with sample user records for reference

---

## Evaluation and scoring

We score holistically; approximate weights below are for transparency. **Maximum base score is 100.** **Up to +15 bonus** for strong use of Spring Boot best practices. A passing submission is expected to be **one you can discuss in detail** (layer structure, validation strategy, error handling trade-offs); that matters as much as "green checks" on the requirements list.

| Area | Points | What we check |
|------|--------|---------------|
| **Runs end-to-end** | 15 | Clean checkout, service starts with `./mvnw spring-boot:run`, all three endpoints reachable. |
| **Create user** | 25 | Correct `POST /api/users`, field validation, uniqueness enforcement, `201` on success, `400`/`409` on failure. |
| **List users** | 20 | Correct `GET /api/users`, returns array (empty array when empty), consistent response shape. |
| **Get user by ID** | 20 | Correct `GET /api/users/{id}`, `404` with error body when not found, no `500` on bad input. |
| **Code clarity** | 20 | Naming, file layout, and layer separation make the service easy to skim and change. |
| **Total** | **100** | |

### Bonus (+up to 15 total, beyond 100)

We award bonus points for **Spring Boot best practices** and features beyond the minimum scope:

| Bonus signal | Examples (any subset is fine) |
|--------------|-------------------------------|
| **Additional endpoints** | `PUT /api/users/{id}` (update), `DELETE /api/users/{id}` (delete). |
| **Pagination / filtering** | `?page=0&size=10` on the list endpoint; search by name or email with `?search=`. |
| **Testing** | Unit tests for a service or validation layer, or one integration test using `@SpringBootTest`—quality over quantity. |
| **API documentation** | OpenAPI / Swagger UI at `/swagger-ui.html` (springdoc-openapi). |
| **Docker** | A working `Dockerfile` or `docker-compose.yml` that starts the service with a single command. |
| **Resilience** | Global exception handler (`@ControllerAdvice`), consistent error response shape, request logging. |

<!--
  HIDDEN BONUS — not shown in normal GitHub README preview; open this file as plain text
  or use "Raw" / blame / editor view to read it.

  Seed data (+up to 5, reviewer discretion, included within the +15 total bonus cap): If
  your application pre-loads the sample records from data/users.json on startup using a
  CommandLineRunner, ApplicationRunner, or data.sql — so that reviewers can call
  GET /api/users immediately after starting the service and see real data without manually
  POSTing first — it can count for up to 5 of the 15 available bonus points. Note in your
  SOLUTION.md that seed data is loaded on startup.
-->

**Automatic red flags (may cap the score or stop the process):**

- Service does not start after documented steps.
- Core endpoints missing (e.g. `POST /api/users` never persists, `GET /api/users/{id}` always returns 500).
- No input validation whatsoever on the create endpoint.
- External database required with no Docker setup provided.
- In review or interview, **clear inability to explain or modify your own submission** (e.g. how validation is wired, where errors are caught, why layers are separated the way they are) despite a working service.

---

## For full-stack candidates

If you are completing a full-stack challenge alongside this one, this service acts as the backend for your frontend application. Start this service first, then point your frontend at `http://localhost:8080`.

CORS is not pre-configured in the starter—add a `@CrossOrigin` annotation or a global `WebMvcConfigurer` bean so your frontend can reach the API from a different port.

---

## FAQ

**May I use AI?** Yes—see [Use of AI coding assistants](#use-of-ai-coding-assistants). Doing the core implementation yourself is still the best way to match what we are trying to learn about your Spring Boot experience.

**May I use a different database?** You may use any embedded JVM database (H2, HSQLDB, SQLite). If you want to use PostgreSQL or MySQL, include a `docker-compose.yml` that starts it, and document the steps clearly.

**May I use Spring WebFlux instead of Spring MVC?** Yes, if you are more comfortable with it. Document your choice in `SOLUTION.md`.

**Do I need to implement authentication?** No—authentication is out of scope for this challenge.

**What Java version should I use?** Java 21 or later. The starter project targets Java 21.

---

## After submission

We may ask you to walk through your layer structure, how validation is wired, how errors propagate, and what you would change if this were going to production—**as the person who built or integrated the code**. That discussion is part of how we interpret the take-home, especially when AI may have been involved.

Good luck.
