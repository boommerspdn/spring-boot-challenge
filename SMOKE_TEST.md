# Smoke Test Results

Run against `http://localhost:8080` on 2026-05-16.

---

## 1. GET /api/users — empty list

```
GET /api/users
Status: 200
```
```json
{"success":true,"data":[],"error":null}
```
✅ Returns empty array, not an error.

---

## 2. POST /api/users — create user 1

```
POST /api/users
Body: {"username":"jsmith","email":"john.smith@example.com","firstName":"John","lastName":"Smith"}
Status: 201
```
```json
{"success":true,"data":{"id":1,"username":"jsmith","email":"john.smith@example.com","firstName":"John","lastName":"Smith","createdAt":"2026-05-16T03:52:28.939922Z"},"error":null}
```
✅ Returns 201 with created user.

---

## 3. POST /api/users — create user 2

```
POST /api/users
Body: {"username":"ajones","email":"alice.jones@example.com","firstName":"Alice","lastName":"Jones"}
Status: 201
```
```json
{"success":true,"data":{"id":2,"username":"ajones","email":"alice.jones@example.com","firstName":"Alice","lastName":"Jones","createdAt":"2026-05-16T03:52:28.968949Z"},"error":null}
```
✅ Returns 201 with created user.

---

## 4. GET /api/users — populated list

```
GET /api/users
Status: 200
```
```json
{"success":true,"data":[{"id":1,"username":"jsmith",...},{"id":2,"username":"ajones",...}],"error":null}
```
✅ Returns array of all users.

---

## 5. GET /api/users/1 — found

```
GET /api/users/1
Status: 200
```
```json
{"success":true,"data":{"id":1,"username":"jsmith","email":"john.smith@example.com","firstName":"John","lastName":"Smith","createdAt":"2026-05-16T03:52:28.939922Z"},"error":null}
```
✅ Returns single user by ID.

---

## 6. GET /api/users/999 — not found

```
GET /api/users/999
Status: 404
```
```json
{"success":false,"data":null,"error":{"status":404,"message":"User not found with id: 999"}}
```
✅ Returns 404 with error body.

---

## 7. GET /api/users/abc — non-numeric ID

```
GET /api/users/abc
Status: 400
```
```json
{"success":false,"data":null,"error":{"status":400,"message":"Invalid value for parameter: id"}}
```
✅ Returns 400, no 500.

---

## 8. POST /api/users — duplicate username

```
POST /api/users
Body: {"username":"jsmith","email":"other@example.com","firstName":"Jane","lastName":"Doe"}
Status: 409
```
```json
{"success":false,"data":null,"error":{"status":409,"message":"username already taken: jsmith"}}
```
✅ Returns 409 with field-level message.

---

## 9. POST /api/users — duplicate email

```
POST /api/users
Body: {"username":"jdoe","email":"john.smith@example.com","firstName":"Jane","lastName":"Doe"}
Status: 409
```
```json
{"success":false,"data":null,"error":{"status":409,"message":"email already taken: john.smith@example.com"}}
```
✅ Returns 409 with field-level message.

---

## 10. POST /api/users — missing required fields

```
POST /api/users
Body: {"username":"jdoe"}
Status: 400
```
```json
{"success":false,"data":null,"error":{"status":400,"message":"email: must not be blank; firstName: must not be blank; lastName: must not be blank"}}
```
✅ Returns 400 with all failing field messages.

---

## Summary

| # | Case | Expected | Actual | Result |
|---|------|----------|--------|--------|
| 1 | GET /api/users (empty) | 200 + `[]` | 200 + `[]` | ✅ |
| 2 | POST create user 1 | 201 | 201 | ✅ |
| 3 | POST create user 2 | 201 | 201 | ✅ |
| 4 | GET /api/users (populated) | 200 + array | 200 + array | ✅ |
| 5 | GET /api/users/1 (found) | 200 | 200 | ✅ |
| 6 | GET /api/users/999 (not found) | 404 | 404 | ✅ |
| 7 | GET /api/users/abc (bad ID) | 400 | 400 | ✅ |
| 8 | POST duplicate username | 409 | 409 | ✅ |
| 9 | POST duplicate email | 409 | 409 | ✅ |
| 10 | POST missing fields | 400 | 400 | ✅ |

**10/10 passed.**
