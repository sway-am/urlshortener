# URL Shortener

A backend-focused URL shortening service built using **Java 17** and **Spring Boot**.
This project is being developed as a 1-week hands-on exercise to apply **backend engineering**
and **system design** concepts.

---

## Features (Planned)
- Generate short URLs for long URLs
- Redirect short URLs to original URLs
- Click count & basic analytics
- Expiring links
- Caching for fast redirects
- Dockerized deployment

---

## Tech Stack
- **Language:** Java 17
- **Framework:** Spring Boot
- **Build Tool:** Maven
- **Database (Dev):** H2 (in-memory)
- **Database (Prod):** PostgreSQL (planned)
- **Cache (Planned):** Redis
- **Version Control:** Git + GitHub
- **OS:** WSL (Ubuntu)

---

## Project Structure
```text
src/
└── main/
    ├── java/
    │   └── org/
    │       └── example/
    │           └── urlshortener/
    │               ├── UrlShortenerApplication.java
    │               ├── controller/     # REST controllers (API layer)
    │               ├── service/        # Business logic
    │               ├── repository/     # JPA repositories
    │               ├── model/          # JPA entities
    │               ├── dto/            # Request/Response DTOs
    │               └── config/         # Configuration classes
    └── resources/
        ├── application.properties
        ├── application-dev.properties
        └── application-prod.properties
```
---

## Architecture (high level)
```text
+----------------------+
|        Client        |
|  (Browser / cURL)    |
+----------+-----------+
           |
           | HTTP / HTTPS
           v
+----------------------+
|   Spring Boot API    |
|  (Stateless Service) |
+----------+-----------+
           |
   +-------+--------+
   |                |
   v                v
+---------+    +----------------+
|  Redis  |    |   PostgreSQL   |
| (Cache) |    | (Primary DB)   |
+---------+    +----------------+
```

## Author
```text
Swayam Rajat Mohanty
B.Tech, Electrical Engineering
(IIT BHU)
```
