# PawaTask

A simple task manager with user authentication, task creation, and comments. Built as part of a technical assignment.

## 🧩 Tech Stack

- **Frontend:** Vue.js, Vuex, Axios, TypeScript
- **Backend:**
    - Java 17 + Spring Boot 3
    - Spring Security (JWT-based auth)
    - JPA (Hibernate) + PostgreSQL
    - Liquibase for DB migrations and seed data
- **Infrastructure:**
    - Docker + Docker Compose
    - NGINX for frontend static hosting

---

## 🚀 Features

- User registration & login
- JWT-based authentication
- Public read-only access to task list & task details (with comments)
- Authenticated users can:
    - Create & update tasks
    - Toggle task status (done / not done)
    - Leave comments on tasks

---

## 🐳 Docker Setup (recommended)

### 1. Run with Docker Compose

Make sure you have Docker and Docker Compose installed.

```bash
docker compose up --build
```

This will spin up:
- PostgreSQL database for user-service and task-service
- Spring Boot-based `user-service` on port `8081`
- Spring Boot-based `task-service` on port `8082`
- Vue.js frontend on port `8080`

Open http://localhost:8080 to use the application.

---

## 🛠 Local Development Setup (without Docker)

### 1. Create local PostgreSQL databases

```bash
# create userdb for user-service
createdb -U postgres userdb

# create taskdb for task-service
createdb -U postgres taskdb
```

### 2. Run Spring Boot services manually

Open two terminal tabs:

```bash
# Terminal 1 - user-service
cd user-service
./gradlew bootRun
```

```bash
# Terminal 2 - task-service
cd task-service
./gradlew bootRun
```

> Note: Make sure PostgreSQL is running and listening on default port (5432).

### 3. Run the frontend

```bash
cd frontend
npm install
npm run serve
```

Then open http://localhost:8080

---

## 🧪 Test Users

You can use the following users to log in without registering:

```text
Username: Viktor K
Password: 123

Username: Irina K
Password: 123
```

---

## 🔑 User Flow

1. **Registration & Login:**
    - `POST /auth/register` → Creates a new user
    - `POST /auth/login` → Returns JWT token
    - `GET /users/me` → Returns user by JWT token

2. **Tasks:**
    - `GET /tasks` → Public list of tasks
    - `POST /tasks` → Create task (auth only)
    - `PUT /tasks/{id}` → Update task (auth only)
    - `PATCH /tasks/{id}/toggle` → Toggle task as done/not done (auth only)
    - `POST /tasks/{id}/comments` → Add comment (auth only)
    - `GET /tasks/{id}` → Load task by id (auth only)

---

## 🛡️ Notes

- All authenticated requests require `Authorization: Bearer &lt;token&gt;`
- Unauthenticated users can only view tasks (read-only)
- Each service has its own isolated PostgreSQL database

---

## 🧪 Testing

The project includes basic integration tests for both services. To run tests:

```bash
./gradlew test
```

## 📄 Assignment

You can find the original task description in the file:

```
Test assignment for Full-Stack Engineer.pdf
```

It is located in the root of the repository.

## 🤝 Author

Viktor Kulygin — [LinkedIn](https://linkedin.com/in/viktorkulygin)

---