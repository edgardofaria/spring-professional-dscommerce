# dsCommerce

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.5.10-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Spring_Security-OAuth2-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" alt="Spring Security OAuth2"/>
  <img src="https://img.shields.io/badge/JWT-Bearer_Token-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT"/>
  <img src="https://img.shields.io/badge/MapStruct-1.6.3-C70D2C?style=for-the-badge" alt="MapStruct"/>
  <img src="https://img.shields.io/badge/H2-In--Memory-1B6AC6?style=for-the-badge&logo=h2&logoColor=white" alt="H2"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="MIT License"/>
</p>

<p align="center">
  A full-featured e-commerce RESTful API built with Spring Boot 3, featuring embedded OAuth2 Authorization Server, JWT authentication, role-based access control, and complete product, order, and user management.
</p>

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [Security & Authentication](#security--authentication)
  - [Authentication Flow](#authentication-flow)
  - [Roles](#roles)
- [API Reference](#api-reference)
  - [Authentication](#authentication)
  - [Products](#products)
  - [Categories](#categories)
  - [Orders](#orders)
  - [Users](#users)
  - [Request & Response Schemas](#request--response-schemas)
  - [Error Responses](#error-responses)
- [Data Model](#data-model)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [API Collection](#api-collection)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

**dsCommerce** is a full-featured e-commerce RESTful API built on Spring Boot 3. The application embeds its own OAuth2 Authorization Server (Spring Authorization Server), issuing JWT tokens for client authentication, and a Resource Server that enforces access control based on roles (`ROLE_ADMIN`, `ROLE_CLIENT`) and custom permission evaluators.

The domain covers products, categories, orders with line items and payments, and users with fine-grained access control.

---

## Features

- **Full product CRUD** — create, read, update, and delete products
- **User management** — public registration, admin or self-service read/edit/delete
- **Order management** — create orders with line items and retrieve by ID with ownership checks
- **Category listing** — public endpoint listing all available categories
- **OAuth2 + JWT authentication** — embedded Authorization Server with custom password grant
- **Role-based access control (RBAC)** — differentiated permissions for `ADMIN` and `CLIENT` roles
- **Custom permission evaluators** — `AuthPermissionEvaluator` enforces ownership checks on orders and users
- **Paginated & filtered listing** — product search by name with Spring Data pagination
- **Centralized error handling** — consistent JSON error format with per-field validation messages
- **Seed data** — 33 products across 5 categories loaded automatically on startup
- **H2 console** — accessible at `/h2-console` during development

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.10 |
| Security | Spring Security + Spring Authorization Server + OAuth2 Resource Server |
| Tokens | JWT (JSON Web Token) |
| Persistence | Spring Data JPA / Hibernate |
| Database (dev) | H2 (in-memory) |
| Validation | Jakarta Bean Validation |
| Object mapping | MapStruct 1.6.3 |
| Boilerplate reduction | Lombok |
| Build | Maven (with `mvnw` wrapper) |
| Monitoring | Spring Boot Actuator |
| Testing | JUnit 5 / Spring Boot Test / Spring Security Test |

---

## Project Structure

```
src/
├── main/
│   ├── java/com/spring_class/dscommerce/
│   │   ├── config/
│   │   │   ├── security/
│   │   │   │   ├── AuthorizationServerConfig.java   # OAuth2 Authorization Server configuration
│   │   │   │   ├── ResourceServerConfig.java        # Resource Server configuration (JWT)
│   │   │   │   ├── SecurityBeansConfig.java         # Security beans (PasswordEncoder, etc.)
│   │   │   │   └── AuthPermissionEvaluator.java     # Custom permission evaluator
│   │   │   └── customGrant/                         # Custom password grant implementation
│   │   ├── controller/
│   │   │   ├── CategoryController.java              # GET /categories
│   │   │   ├── OrderController.java                 # GET /orders/{id}, POST /orders
│   │   │   ├── ProductController.java               # CRUD /products
│   │   │   ├── UserController.java                  # CRUD /users
│   │   │   └── handlers/
│   │   │       └── ControllerExceptionHandler.java  # Global exception handler
│   │   ├── dto/
│   │   │   ├── category/
│   │   │   ├── order/
│   │   │   ├── orderItem/
│   │   │   ├── payment/
│   │   │   ├── product/
│   │   │   ├── role/
│   │   │   ├── user/
│   │   │   └── errors/
│   │   ├── entities/
│   │   │   ├── BaseEntity.java         # Abstract entity with auto-generated ID
│   │   │   ├── Authority.java
│   │   │   ├── Category.java
│   │   │   ├── Order.java
│   │   │   ├── OrderItem.java / OrderItemPK.java
│   │   │   ├── OrderStatus.java        # Enum: WAITING_PAYMENT, PAID, SHIPPED...
│   │   │   ├── Payment.java
│   │   │   ├── Product.java
│   │   │   ├── Role.java
│   │   │   └── User.java
│   │   ├── mapper/
│   │   │   └── ProductMapper.java      # MapStruct interface
│   │   ├── repositories/
│   │   │   ├── CategoryRepository.java
│   │   │   └── ProductRepository.java
│   │   └── service/
│   │       ├── CategoryService.java
│   │       ├── OrderService.java
│   │       ├── ProductService.java
│   │       ├── UserService.java
│   │       └── exceptions/
│   └── resources/
│       ├── application.properties
│       ├── application-test.properties   # H2 dev/test profile
│       └── import.sql                    # Seed: 33 products, 5 categories, users
└── test/
    └── java/com/spring_class/dscommerce/
        └── DscommerceApplicationTests.java
```

---

## Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.8+** — included via the `mvnw` wrapper (no local install required)
- **Git**

### Installation

```bash
# Clone the repository
git clone https://github.com/edgardofaria/dsEcommerce.git
cd dsEcommerce
```

### Running the Application

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The API will be available at **`http://localhost:8080`**.

The H2 web console is accessible at **`http://localhost:8080/h2-console`** with the following credentials:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:testdb` |
| Username | `sa` |
| Password | *(leave empty)* |

> **Note:** The application starts with the `test` profile by default, which uses an in-memory H2 database. All data is seeded from `import.sql` on every startup and is lost when the application stops.

---

## Security & Authentication

### Authentication Flow

The application uses the **OAuth2** standard with an embedded Authorization Server (Spring Authorization Server). The flow is a **custom password grant**:

1. The client sends credentials (username + password) via `POST /oauth2/token` using HTTP Basic Auth with the `client-id` and `client-secret`.
2. The Authorization Server validates the credentials and issues a **JWT** access token.
3. The client includes the token in every protected request:
   ```
   Authorization: Bearer <token>
   ```
4. The Resource Server validates the JWT and enforces access restrictions based on the user's role.

### Roles

| Role | Description |
|---|---|
| `ROLE_ADMIN` | Full access: manages products and users, views any order |
| `ROLE_CLIENT` | Restricted access: creates orders, reads and edits own data |

Beyond roles, the `AuthPermissionEvaluator` enforces ownership — a `CLIENT` can only access or edit their own orders and user data.

---

## API Reference

Base URL: `http://localhost:8080`

> **Access legend:** `Public` = no authentication required | `Auth` = any authenticated user | `ADMIN` = admin role only | `ADMIN / owner` = admin or the resource owner

### Authentication

| Method | Endpoint | Description | Access | Status |
|---|---|---|---|---|
| `POST` | `/oauth2/token` | Obtain JWT token (password grant) | Public (Basic Auth) | `200 OK` |

**Example request:**

```bash
curl -X POST http://localhost:8080/oauth2/token \
  -u "myclientid:myclientsecret" \
  -d "grant_type=password&username=maria@gmail.com&password=123456"
```

**Response:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiJ9...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

---

### Products

| Method | Endpoint | Description | Access | Status |
|---|---|---|---|---|
| `GET` | `/products` | List products (paginated, filterable by name) | Public | `200 OK` |
| `GET` | `/products/{id}` | Get product by ID | Public | `200 OK` |
| `POST` | `/products` | Create a product | ADMIN | `201 Created` |
| `PUT` | `/products/{id}` | Update a product | ADMIN | `200 OK` |
| `DELETE` | `/products/{id}` | Delete a product | ADMIN | `204 No Content` |

#### Query Parameters — `GET /products`

| Parameter | Type | Required | Description |
|---|---|---|---|
| `name` | `string` | No | Case-insensitive substring filter on product name |
| `page` | `integer` | No | Page number (0-indexed, default: `0`) |
| `size` | `integer` | No | Page size (default: `12`) |
| `sort` | `string` | No | Sort field and direction (e.g., `name,asc`) |

```
GET /products?name=notebook&page=0&size=10&sort=name,asc
```

---

### Categories

| Method | Endpoint | Description | Access | Status |
|---|---|---|---|---|
| `GET` | `/categories` | List all categories | Public | `200 OK` |

---

### Orders

| Method | Endpoint | Description | Access | Status |
|---|---|---|---|---|
| `GET` | `/orders/{id}` | Get order by ID | ADMIN / owner | `200 OK` |
| `POST` | `/orders` | Create a new order | Auth | `201 Created` |

---

### Users

| Method | Endpoint | Description | Access | Status |
|---|---|---|---|---|
| `GET` | `/users/me` | Get the currently authenticated user | Auth | `200 OK` |
| `GET` | `/users` | List all users (paginated, filterable by name) | ADMIN | `200 OK` |
| `GET` | `/users/{id}` | Get user by ID | ADMIN | `200 OK` |
| `POST` | `/users` | Register a new user | Public | `201 Created` |
| `PUT` | `/users/{id}` | Update user data | ADMIN / owner | `200 OK` |
| `DELETE` | `/users/{id}` | Delete a user | ADMIN / owner | `204 No Content` |

---

### Request & Response Schemas

#### `ProductRequestDTO` — used in `POST /products` and `PUT /products/{id}`

```json
{
  "name": "MacBook Pro M3",
  "description": "Apple MacBook Pro with M3 Pro chip, 18GB RAM, 512GB SSD.",
  "price": 12999.90,
  "imgUrl": "https://example.com/macbook.jpg",
  "categoriesId": [3]
}
```

| Field | Type | Constraints |
|---|---|---|
| `name` | `string` | Required, 3–80 characters |
| `description` | `string` | Required, minimum 10 characters |
| `price` | `number` | Required, must be positive |
| `imgUrl` | `string` | Required, not blank |
| `categoriesId` | `array<integer>` | Required, list of existing category IDs |

#### `ProductDTO` — response for `GET`, `POST`, `PUT`

```json
{
  "id": 1,
  "name": "MacBook Pro M3",
  "description": "Apple MacBook Pro with M3 Pro chip, 18GB RAM, 512GB SSD.",
  "price": 12999.90,
  "imgUrl": "https://example.com/macbook.jpg",
  "categories": [
    { "id": 3, "name": "Computers" }
  ]
}
```

#### Paginated response — `GET /products`

```json
{
  "content": [ "...array of ProductMinDTO..." ],
  "page": {
    "size": 12,
    "number": 0,
    "totalElements": 33,
    "totalPages": 3
  }
}
```

---

### Error Responses

All errors follow a consistent JSON structure.

#### Standard error (`4xx`)

```json
{
  "timestamp": "2026-03-22T12:00:00Z",
  "status": 404,
  "error": "Resource not found",
  "path": "/products/999"
}
```

#### Validation error (`422`)

```json
{
  "timestamp": "2026-03-22T12:00:00Z",
  "status": 422,
  "errors": [
    { "field": "name", "message": "must not be blank" },
    { "field": "price", "message": "must be greater than 0" }
  ],
  "path": "/products"
}
```

| HTTP Status | Cause |
|---|---|
| `401 Unauthorized` | Missing or invalid token |
| `403 Forbidden` | Authenticated user lacks permission for the resource |
| `404 Not Found` | Resource not found |
| `422 Unprocessable Entity` | Bean validation failure or database integrity violation |

---

## Data Model

```
┌──────────┐     ┌───────────┐     ┌──────────────┐     ┌──────────┐
│   User   │────<│   Order   │────<│  OrderItem   │>────│ Product  │
└──────────┘     └───────────┘     └──────────────┘     └──────────┘
     │                │                                       │
 ┌───┴───┐       ┌────┴────┐                           ┌──────┴──────┐
 │  Role │       │ Payment │                           │  Category   │
 └───────┘       └─────────┘                           └─────────────┘
```

### Tables

| Table | Description |
|---|---|
| `tb_user` | Registered users |
| `tb_role` | Access roles (`ROLE_ADMIN`, `ROLE_CLIENT`) |
| `tb_user_role` | User ↔ Role relationship (many-to-many) |
| `tb_product` | Product catalog |
| `tb_category` | Product categories |
| `tb_product_category` | Product ↔ Category relationship (many-to-many) |
| `tb_order` | Customer orders |
| `tb_order_item` | Line items within an order (composite PK) |
| `tb_payment` | Payment record (shares PK with `tb_order`) |

### Seed Categories

| ID | Name |
|---|---|
| 1 | Electronics |
| 2 | Books |
| 3 | Computers |
| 4 | Games |
| 5 | Clothing |

---

## Configuration

Configuration is managed via Spring Profiles and environment variables.

### `application.properties`

```properties
spring.application.name=dscommerce
spring.profiles.active=test
spring.jpa.open-in-view=false

security.client-id=${CLIENT_ID:myclientid}
security.client-secret=${CLIENT_SECRET:myclientsecret}
security.jwt.duration=${JWT_DURATION:3600}

cors.origins=${CORS_ORIGINS:http://localhost:3000,http://localhost:5173}
```

### Environment Variables

| Variable | Default | Description |
|---|---|---|
| `CLIENT_ID` | `myclientid` | OAuth2 client identifier |
| `CLIENT_SECRET` | `myclientsecret` | OAuth2 client secret |
| `JWT_DURATION` | `3600` | JWT token duration in seconds |
| `CORS_ORIGINS` | `http://localhost:3000,...` | Allowed CORS origins |

### `application-test.properties` (active by default)

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
```

To connect a persistent database (e.g., PostgreSQL), create an `application-prod.properties` file and activate it with:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## Running Tests

```bash
# Run all tests
./mvnw test

# Run tests with coverage report
./mvnw test jacoco:report
```

> The project currently includes a smoke test that verifies the application context loads successfully. Unit tests for the service layer and integration tests for the controller layer are planned in the roadmap.

---

## API Collection

The `collections/` directory contains ready-to-use **Bruno** collection files for manual testing of all endpoints.

To use them, open [Bruno](https://www.usebruno.com/), choose **Open Collection**, and point it to the `collections/` folder.

The collection is organized into two groups:

**Authorization Server**

| File | Description |
|---|---|
| `Login.yml` | Log in as a regular user (CLIENT) |
| `Login Admin.yml` | Log in as an administrator (ADMIN) |

**Resource Server**

| Folder | File | Description |
|---|---|---|
| Products | `All products (public).yml` | List all products (public) |
| Products | `Product by id (public).yml` | Get product by ID (public) |
| Products | `Product by id (auth).yml` | Get product by ID (authenticated) |
| Products | `New product (admin only).yml` | Create a product |
| Products | `Update product (admin only).yml` | Update a product |
| Products | `Delete product (admin only).yml` | Delete a product |
| Categories | `All categories (public).yml` | List all categories (public) |
| Orders | `New order (auth).yml` | Create an order |
| Orders | `Order by id (admin only).yml` | Get order by ID |
| Users | `New user (public).yml` | Register a user |
| Users | `Current user (auth).yml` | Get the authenticated user |
| Users | `All users (admin only).yml` | List all users |
| Users | `User by id (admin only).yml` | Get user by ID |
| Users | `Update user (admin or client).yml` | Update a user |
| Users | `Delete user (admin or client).yml` | Delete a user |

---

## Roadmap

- [ ] Production profile with PostgreSQL and `application-prod.properties`
- [ ] Unit test suite (service layer) and integration tests (controller layer)
- [ ] Interactive API documentation with Springdoc OpenAPI / Swagger UI
- [ ] Containerization with Docker and Docker Compose
- [ ] Payment gateway integration
- [ ] Advanced pagination and filters for order and user endpoints

---

## License

This project is licensed under the [MIT License](LICENSE).
