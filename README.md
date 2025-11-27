
# Expense Tracker API — Spring Boot Assignment

A simple Expense Tracker REST API built using Spring Boot, designed as part of a 2-day backend assignment.  
The application supports CRUD operations, sorting, category-wise aggregation, DTO-based APIs, validation, exception handling, unit tests, and integration tests — all backed by an in-memory H2 database.

---

## Tech Stack
- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- H2 In-Memory Database
- Lombok
- JUnit 5 + Mockito
- MockMvc
- Maven

---

## Running Locally

Before running this project, ensure you have:

1. **Java 17 or above** installed (required).
2. **Git** installed (to clone the project).

### 1. Clone the repository
```bash
git clone https://github.com/kedarnelavelli/expense-tracker
cd expense-tracker
```

### 2. Run
```bash
./mvnw spring-boot:run
```

### Base URL:
```
http://localhost:8080
```

---

## H2 Database Console

**URL:**  
```
http://localhost:8080/h2-console
```

**JDBC URL:**  
```
jdbc:h2:mem:expensedb
```

No password required.

---

# API Endpoints With Sample Requests & Responses

---

#  Create Expense  
### **POST** `/api/expenses`

### Request Body
```json
{
  "category": "FOOD",
  "amount": 50,
  "date": "2025-12-22T00:00:00+05:30",
  "notes": "Refreshers"
}
```

### Response (201 CREATED)
```json
{
  "id": 3,
  "category": "FOOD",
  "amount": 50,
  "date": "2025-12-21T18:30:00Z",
  "notes": "Refreshers"
}
```

---

#  Get All Expenses  
### **GET** `/api/expenses`

### Response
```json
{
  "content": [
    {
      "id": 1,
      "category": "FOOD",
      "amount": 300.00,
      "date": "2025-12-21T18:30:00Z",
      "notes": "Updated lunch"
    },
    {
      "id": 2,
      "category": "FOOD",
      "amount": 50.00,
      "date": "2025-12-21T18:30:00Z",
      "notes": "Refreshers"
    },
    {
      "id": 3,
      "category": "FOOD",
      "amount": 50.00,
      "date": "2025-12-21T18:30:00Z",
      "notes": "Refreshers"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 3,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 3,
  "empty": false
}
```

### Sorting  
`GET /api/expenses?sortBy=amount`

---

#  Update Expense  
### **PUT** `/api/expenses/{id}`

### Request
```json
{
  "id" : 1,
  "category": "FOOD",
  "amount": 300,
  "date": "2025-12-22T00:00:00+05:30",
  "notes": "Updated lunch"
}
```

### Response
```json
{
  "id": 1,
  "category": "FOOD",
  "amount": 300,
  "date": "2025-12-21T18:30:00Z",
  "notes": "Updated lunch"
}
```

---

#  Top Spending Categories  
### **GET** `/api/expenses/top`

### Response
```json
[
  {
    "category": "FOOD",
    "total": 400.00
  }
]
```

---

#  Validation Errors

### Request
```json
{
  "amount": -50
}
```

### Response (400)
```json
{
  "timestamp": "2025-11-22T10:35:00",
  "status": 400,
  "message": "Validation failed",
  "errors": [
    "category: Category is required",
    "amount: Amount must be positive"
  ]
}
```

---

Run tests:
```bash
.\mvnw.cmd test
```


