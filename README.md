# Spring Boot JWT Authentication API

A robust, scalable, and highly available backend authentication service built with Spring Boot. This project serves as a foundational API for secure user authentication, featuring a rock-solid containerized database architecture, advanced logging, email verification, and a centralized error management system.

## 🚀 Architecture & Features Implemented

### 1. Database & Containerization Architecture
* Integrated **PostgreSQL** (`auth_db`) as the primary relational database.
* Successfully containerized and deployed the database environment using **Docker**. By configuring specific platform tags and utilizing Alpine-based images, we successfully overcame WSL2 architecture mismatches (`exec format error`), ensuring a fully isolated, reproducible, and modern local development environment.

### 2. Email-Based Account Activation
* Implemented a secure user registration workflow featuring **email verification**. 
* New users are required to verify their email addresses via a unique confirmation link sent to their inbox to activate their accounts. This ensures data integrity, validates user identity, and prevents spam registrations.

### 3. Advanced Logging Strategy (Log4j2)
* Replaced the default Spring Boot logger with **Log4j2** for high-performance and comprehensive application logging.
* Configured file-based appenders to persistently record system events, errors, user activities, and application states into dedicated log files, ensuring reliable monitoring and easier debugging in production environments.

### 4. Global Exception Handling
Implemented a centralized error-handling architecture to prevent stack-trace leaks and provide standardized API responses to the frontend.
* **`@RestControllerAdvice`:** Created a `GlobalExceptionHandler` to intercept exceptions globally across all controllers.
* **Standardized JSON Responses:** Designed a `GenericResponseDto` to wrap all errors (and future success messages) into a consistent, predictable JSON format.

### 5. Custom Exception Strategy (Clean Code)
* Developed business-specific exceptions like `AuthenticationException`.
* Inherited from `RuntimeException` (Unchecked Exception) to adhere to Clean Code principles. This strategic choice prevents the codebase from being cluttered with redundant `try-catch` blocks or forced method signatures.

### 6. Internationalization (i18n) & MessageSource
* Externalized all error messages and system texts using Spring's `MessageSource` interface.
* Enabled dynamic message resolution based on user `Locale`. For instance, abstract error codes like `AUTH_001` are dynamically translated into user-friendly messages without hardcoding text into the Java classes.

### 7. API Testing & Endpoint Validation
* Utilized **Postman** to simulate client requests, validate response payloads, test database connections, and rigorously verify the exception-handling edge cases across all API endpoints.

## 🛠️ Tech Stack
* **Java** (Core Language)
* **Spring Boot** (Web, Data JPA, Mail)
* **PostgreSQL** (Relational Database)
* **Docker** (Containerization)
* **Log4j2** (Advanced Logging)
* **Postman** (API Testing & Debugging)
* **Lombok** (For reducing boilerplate code)

## 📌 Upcoming Features (Roadmap)
* User Entity and Database Table Mapping
* JWT (JSON Web Token) Generation and Validation
* Spring Security Integration for Role-Based Authorization

---
**Developer:** Mirzat Ceylan
