# Smoke Test Results

Run against `http://localhost:8080` on 2026-05-16.

---

## 1. GET /api/users — with seed data

```
GET /api/users
Status: 200
```
```json
{
    "success": true,
    "data": [
        {"id":1,"username":"jsmith","email":"john.smith@example.com","firstName":"John","lastName":"Smith","createdAt":"2026-05-16T04:27:41.899417Z"},
        {"id":2,"username":"ejohnson","email":"emily.johnson@example.com","firstName":"Emily","lastName":"Johnson","createdAt":"2026-05-16T04:27:41.911998Z"},
        {"id":3,"username":"mbrown","email":"michael.brown@example.com","firstName":"Michael","lastName":"Brown","createdAt":"2026-05-16T04:27:41.913498Z"},
        {"id":4,"username":"swilliams","email":"sarah.williams@example.com","firstName":"Sarah","lastName":"Williams","createdAt":"2026-05-16T04:27:41.914904Z"},
        {"id":5,"username":"djones","email":"david.jones@example.com","firstName":"David","lastName":"Jones","createdAt":"2026-05-16T04:27:41.916303Z"}
    ],
    "error": null
}
```
✅ Returns seed users loaded from `data/users.json` on startup.

---

## 2. GET /api/users?search=john — filtering

```
GET /api/users?search=john
Status: 200
```
```json
{
    "success": true,
    "data": [
        {"id":1,"username":"jsmith","email":"john.smith@example.com","firstName":"John","lastName":"Smith","createdAt":"2026-05-16T04:27:41.899417Z"},
        {"id":2,"username":"ejohnson","email":"emily.johnson@example.com","firstName":"Emily","lastName":"Johnson","createdAt":"2026-05-16T04:27:41.911998Z"}
    ],
    "error": null
}
```
✅ Returns users matching "john" in firstName, lastName, or email (case-insensitive).

---

## 3. GET /api/users?page=0&size=2 — pagination

```
GET /api/users?page=0&size=2
Status: 200
```
```json
{
    "success": true,
    "data": [
        {"id":1,"username":"jsmith","email":"john.smith@example.com","firstName":"John","lastName":"Smith","createdAt":"2026-05-16T04:27:41.899417Z"},
        {"id":2,"username":"ejohnson","email":"emily.johnson@example.com","firstName":"Emily","lastName":"Johnson","createdAt":"2026-05-16T04:27:41.911998Z"}
    ],
    "error": null
}
```
✅ Returns first page of 2 users.

---

## 4. POST /api/users — create user

```
POST /api/users
Body: {"username":"ajones","email":"alice.jones@example.com","firstName":"Alice","lastName":"Jones"}
Status: 201
```
```json
{"success":true,"data":{"id":6,"username":"ajones","email":"alice.jones@example.com","firstName":"Alice","lastName":"Jones","createdAt":"2026-05-16T04:27:41.968949Z"},"error":null}
```
✅ Returns 201 with created user.

---

## 5. GET /api/users/1 — found

```
GET /api/users/1
Status: 200
```
```json
{"success":true,"data":{"id":1,"username":"jsmith","email":"john.smith@example.com","firstName":"John","lastName":"Smith","createdAt":"2026-05-16T04:27:41.899417Z"},"error":null}
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

## 11. PUT /api/users/1 — update success

```
PUT /api/users/1
Body: {"username":"johnsmith","email":"john.smith@example.com","firstName":"Jonathan","lastName":"Smith"}
Status: 200
```
```json
{
    "success": true,
    "data": {
        "id": 1,
        "username": "johnsmith",
        "email": "john.smith@example.com",
        "firstName": "Jonathan",
        "lastName": "Smith",
        "createdAt": "2026-05-16T04:27:41.899417Z"
    },
    "error": null
}
```
✅ Returns 200 with updated user. `createdAt` is unchanged.

---

## 12. PUT /api/users/999 — not found

```
PUT /api/users/999
Body: {"username":"ghost","email":"ghost@example.com","firstName":"Ghost","lastName":"User"}
Status: 404
```
```json
{"success":false,"data":null,"error":{"status":404,"message":"User not found with id: 999"}}
```
✅ Returns 404 with error body.

---

## 13. PUT /api/users/1 — duplicate username

```
PUT /api/users/1
Body: {"username":"ejohnson","email":"john.smith@example.com","firstName":"Jonathan","lastName":"Smith"}
Status: 409
```
```json
{"success":false,"data":null,"error":{"status":409,"message":"username already taken: ejohnson"}}
```
✅ Returns 409 when username conflicts with a different user.

---

## 14. PUT /api/users/1 — missing required fields

```
PUT /api/users/1
Body: {"username":"johnsmith"}
Status: 400
```
```json
{"success":false,"data":null,"error":{"status":400,"message":"email: must not be blank; firstName: must not be blank; lastName: must not be blank"}}
```
✅ Returns 400 with all failing field messages.

---

## 15. DELETE /api/users/5 — success

```
DELETE /api/users/5
Status: 204
```
_(no body)_

✅ Returns 204 No Content.

---

## 16. DELETE /api/users/999 — not found

```
DELETE /api/users/999
Status: 404
```
```json
{"success":false,"data":null,"error":{"status":404,"message":"User not found with id: 999"}}
```
✅ Returns 404 with error body.

---

## Summary

| # | Case | Expected | Actual | Result |
|---|------|----------|--------|--------|
| 1 | GET /api/users (seed data) | 200 + 5 users | 200 + 5 users | ✅ |
| 2 | GET /api/users?search=john | 200 + 2 matches | 200 + 2 matches | ✅ |
| 3 | GET /api/users?page=0&size=2 | 200 + 2 users | 200 + 2 users | ✅ |
| 4 | POST create user | 201 | 201 | ✅ |
| 5 | GET /api/users/1 (found) | 200 | 200 | ✅ |
| 6 | GET /api/users/999 (not found) | 404 | 404 | ✅ |
| 7 | GET /api/users/abc (bad ID) | 400 | 400 | ✅ |
| 8 | POST duplicate username | 409 | 409 | ✅ |
| 9 | POST duplicate email | 409 | 409 | ✅ |
| 10 | POST missing fields | 400 | 400 | ✅ |
| 11 | PUT /api/users/1 (success) | 200 + updated user | 200 + updated user | ✅ |
| 12 | PUT /api/users/999 (not found) | 404 | 404 | ✅ |
| 13 | PUT duplicate username | 409 | 409 | ✅ |
| 14 | PUT missing fields | 400 | 400 | ✅ |
| 15 | DELETE /api/users/5 (success) | 204 | 204 | ✅ |
| 16 | DELETE /api/users/999 (not found) | 404 | 404 | ✅ |

**16/16 passed.**
