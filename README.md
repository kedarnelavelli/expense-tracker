
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
  "category": "Food",
  "amount": 250,
  "date": "2025-11-22T10:00:00",
  "notes": "Lunch at cafe"
}
```

### Response (201 CREATED)
```json
{
  "id": 1,
  "category": "Food",
  "amount": 250,
  "date": "2025-11-22T10:00:00",
  "notes": "Lunch at cafe"
}
```

---

#  Get All Expenses  
### **GET** `/api/expenses`

### Response
```json
[
  {
    "id": 1,
    "category": "Food",
    "amount": 250,
    "date": "2025-11-22T10:00:00",
    "notes": "Lunch at cafe"
  },
  {
    "id": 2,
    "category": "Travel",
    "amount": 500,
    "date": "2025-11-21T08:30:00",
    "notes": "Cab ride"
  }
]
```

### Sorting  
`GET /api/expenses?sortBy=amount`

---

#  Update Expense  
### **PUT** `/api/expenses/{id}`

### Request
```json
{
  "category": "Travel",
  "amount": 300,
  "date": "2025-11-23T12:00:00",
  "notes": "Updated travel"
}
```

### Response
```json
{
  "id": 2,
  "category": "Travel",
  "amount": 300,
  "date": "2025-11-23T12:00:00",
  "notes": "Updated travel"
}
```

---

#  Top Spending Categories  
### **GET** `/api/expenses/top`

### Response
```json
[
  { "category": "Food", "total": 600 },
  { "category": "Travel", "total": 400 },
  { "category": "Grocery", "total": 200 }
]
```

---

#  Validation Errors

### Request
```json
{
  "category": "",
  "amount": -50,
  "date": "invalid"
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
    "amount: Amount must be positive",
    "date: must be in ISO date-time format"
  ]
}
```

---

Run tests:
```bash
.\mvnw.cmd test
```


