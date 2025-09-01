# 🏋️‍♂️ Fitness Microservices Application

A **comprehensive full-stack fitness tracker** built with **Spring Boot microservices**, a **React frontend**, and **AI-powered recommendations**.  This project demonstrates how to design **scalable, secure, event-driven applications** with modern Java + React tooling.

---

## ✨ Features

* **Full-Stack Development** — Spring Boot backend + React frontend.
* **Microservices Architecture** — Independent services (User, Activity, AI) for modularity & scalability.
* **AI-Powered Recommendations** — Integrates **Google Gemini API** for personalized health feedback.
* **Robust Security** — OAuth2 PKCE with **Keycloak**, JWT validation, and user sync between Keycloak & User Service.
* **Asynchronous Communication** — **RabbitMQ** handles event-driven activity → AI workflows.
* **Service Discovery** — **Eureka Server** for dynamic registration & discovery.
* **API Gateway** — Central entry point with security enforcement and routing.
* **Centralized Configuration** — **Spring Cloud Config** server with Git-backed configs.
* **Polyglot Persistence** — PostgreSQL (User Service), MongoDB (Activity + AI Services).
* **Logging & Monitoring** — Integration points for observability.

---

## 🏗️ Architecture

```text
React Frontend → API Gateway → User / Activity / AI Services
                       │
                       ├── Eureka Server (Service Registry)
                       ├── Config Server (Central Configs)
                       ├── Keycloak (OAuth2 + JWT)
                       └── RabbitMQ (Async Events)
```

* **User Service** → user profiles in PostgreSQL + Keycloak sync
* **Activity Service** → activity logs in MongoDB, publishes events to RabbitMQ
* **AI Service** → consumes events, calls Gemini API, stores recommendations
* **API Gateway** → routes, validates tokens, applies CORS/security
* **Config Server** → centralized YAML configs for all services
* **Eureka Server** → service discovery & load balancing

---

## 🛠️ Tech Stack

**Backend**

* Java 17+, Spring Boot, Spring Cloud (Eureka, Gateway, Config)
* PostgreSQL, MongoDB, RabbitMQ
* Keycloak (OAuth2 PKCE)
* Maven, Lombok

**Frontend**

* React (Vite)
* Redux Toolkit, React Router DOM
* Material-UI
* Axios, react-oauth2-code-pkce

**AI**

* Google Gemini API (Generative AI for recommendations)

**Tools**

* Docker & Docker Compose (Keycloak, RabbitMQ, DBs)
* IntelliJ IDEA / VS Code
* Postman, MongoDB Compass, pgAdmin

---

## 🚀 Getting Started

### 1. Clone

```bash
git clone <repository-url>
cd fitness-microservices
```

### 2. Docker Setup (Keycloak & RabbitMQ)

```bash
# Keycloak (admin: admin / admin)
docker run -p 8181:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:23.0.4 start-dev

# RabbitMQ (guest/guest at http://localhost:15672)
docker run -d --hostname my-rabbit --name rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:3.12-management
```

### 3. Databases

* **PostgreSQL** → create DB `fitness_user_DB`
* **MongoDB** → create DBs `fitness_activity` and `fitness_recommendation`

### 4. Google Gemini API Key

Obtain from [Google AI Studio](https://makersuite.google.com/).
Set environment variables:

```bash
export GEMINI_API_URL=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent
export GEMINI_API_KEY=<your-key>
```

### 5. Backend Services

* Start **Eureka Server** (`http://localhost:8761`)
* Start **Config Server** (`http://localhost:8888`)
* Start **User Service** (Postgres)
* Start **Activity Service** (MongoDB + RabbitMQ)
* Start **AI Service** (MongoDB + Gemini API)
* Start **API Gateway** (`http://localhost:8080`)

### 6. Keycloak Setup

* Realm: `fitness-oauth2`
* Client: `oauth2-pkce-client` (public, PKCE S256)
* Redirect URIs: `http://localhost:5173/*`
* Web Origins: `http://localhost:5173`
* Test user: `user1 / password1`

### 7. Frontend

```bash
cd fitness-app-frontend
npm install
npm run dev
```

Visit 👉 `http://localhost:5173`

---

## 💻 Usage

1. **Login** → via Keycloak.
2. **Dashboard** → view activities.
3. **Add Activity** → type, duration, calories, metrics.
4. **Async AI Recommendation** → AI service consumes events, saves recommendations.
5. **View Details** → activity details + AI recommendations, improvements, suggestions, safety tips.
6. **Logout** → clears session.

---

## 📂 Project Structure

```text
fitness-microservices/
├── fitness-app-frontend/   # React frontend
├── api-gateway/            # Spring Cloud Gateway
├── ai-service/             # AI microservice
├── activity-service/       # Activity microservice
├── user-service/           # User microservice
├── config-server/          # Centralized config
│   └── resources/config/   # service configs
├── eureka-server/          # Service registry
└── pom.xml                 # Parent Maven build
```

---

## 🤝 Contributing
Contributions are welcome!
* Fork the repo
* Create a feature branch
* Submit a PR

---
## Acknowledgments
This Fitness Microservices Application is based on a project created by https://github.com/EmbarkXOfficial.  
I used it for learning purposes to understand Spring Boot microservices, Keycloak authentication, and AI service integration.  
All credit goes to the original author for their foundational work.

