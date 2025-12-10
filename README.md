# E-Commerce Backend Application

A comprehensive Spring Boot-based e-commerce backend system with JWT authentication, role-based access control, Stripe payment integration, and RESTful APIs.

## Table of Contents
- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Features](#features)
- [Project Structure](#project-structure)
- [Setup & Installation](#setup--installation)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Database Schema](#database-schema)
- [Configuration](#configuration)

## Overview

This is a full-featured e-commerce backend application built with Spring Boot that provides:
- Multi-role user management (User, Seller, Admin)
- Product catalog with categories
- Shopping cart functionality
- Order management and processing
- Stripe payment integration
- JWT-based authentication
- RESTful API endpoints

## Technology Stack

### Core Framework
- **Spring Boot 3.x** - Main application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **Hibernate** - ORM implementation

### Security & Authentication
- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Password encryption
- **HTTP-only Cookies** - Secure token storage

### Payment Processing
- **Stripe API** - Payment gateway integration

### Other Libraries
- **ModelMapper** - Object mapping (DTO ↔ Entity)
- **Lombok** - Boilerplate code reduction
- **Dotenv** - Environment variable management
- **Jakarta Validation** - Input validation

### Database
- Configurable (PostgreSQL/MySQL/H2)
- JPA/Hibernate for ORM

## Architecture

### Design Pattern
The application follows a **layered architecture**:

```
Controller Layer (REST endpoints)
      ↓
Service Layer (Business logic)
      ↓
Repository Layer (Data access)
      ↓
Database
```

### Key Components

**Controllers**: Handle HTTP requests and responses
**Services**: Implement business logic
**Repositories**: Manage database operations
**DTOs**: Data transfer objects for API communication
**Entities**: JPA entities mapping to database tables
**Security**: JWT authentication and authorization

## Features

### 1. User Management
- User registration with role assignment (User, Seller, Admin)
- JWT-based authentication
- Role-based access control
- User profile management

### 2. Product Management
- CRUD operations for products
- Category-based organization
- Product search by keyword
- Image upload functionality
- Pagination and sorting
- Discount and special pricing

### 3. Shopping Cart
- Add/remove products
- Update quantities
- Cart persistence per user
- Automatic price calculation
- Bulk cart creation/update

### 4. Order Processing
- Place orders with address selection
- Payment method integration
- Order status tracking
- Order history
- Seller-specific order filtering
- Admin order management

### 5. Address Management
- Multiple addresses per user
- CRUD operations
- Address association with orders

### 6. Payment Integration
- Stripe payment gateway
- Payment intent creation
- Customer management in Stripe
- Secure payment processing

### 7. Category Management
- Product categorization
- Category CRUD operations
- Category-based product filtering

## Project Structure

```
src/main/java/com/ecombackend/
├── config/
│   └── AppConfig.java                    # Application configuration
├── constants/
│   └── AppConstants.java                 # Application constants
├── controller/                           # REST controllers
│   ├── AddressController.java
│   ├── AuthController.java
│   ├── CartController.java
│   ├── CategoryController.java
│   ├── OrderController.java
│   └── ProductController.java
├── dto/                                  # Data Transfer Objects
│   ├── AddressDTO.java
│   ├── CartDTO.java
│   ├── OrderDTO.java
│   ├── ProductDTO.java
│   └── [other DTOs]
├── entity/                               # JPA Entities
│   ├── User.java
│   ├── Product.java
│   ├── Order.java
│   ├── Cart.java
│   └── [other entities]
├── exception/                            # Exception handling
│   ├── APIException.java
│   ├── ResourceNotFoundException.java
│   └── MyGlobalExceptionHandler.java
├── repository/                           # Data repositories
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   └── [other repositories]
├── security/                             # Security configuration
│   ├── SecurityConfig.java
│   ├── jwt/
│   │   ├── JwtUtils.java
│   │   ├── AuthTokenFilter.java
│   │   └── AuthEntryPointJwt.java
│   └── service/
│       └── UserDetailsServiceImpl.java
└── service/                              # Business logic
    ├── [interfaces]
    └── impl/
        └── [implementations]
```

## Setup & Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL/MySQL (or use H2 for development)
- Stripe account (for payment processing)

### Environment Variables

Create a `.env` file in the root directory:

```properties
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ecommerce_db
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

# JPA Configuration
SPRING_JPA_HIBERNATE_DDL_AUTO=update

# JWT Configuration
SPRING_APP_JWTSECRET=your_secret_key_here_at_least_256_bits
SPRING_APP_JWTEXPIRATIONMS=86400000
SPRING_ECOM_APP_JWTCOOKIENAME=ecom_jwt

# File Upload
PROJECT_IMAGE=images/

# Stripe
STRIPE_SECRET_KEY=your_stripe_secret_key
```

### Installation Steps

1. **Clone the repository**
```bash
git clone <repository-url>
cd ecom-backend
```

2. **Configure environment variables**
```bash
# Create .env file with the variables above
```

3. **Build the project**
```bash
mvn clean install
```

4. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Default Users

The application creates three default users on startup:

| Username | Email | Password | Role |
|----------|-------|----------|------|
| user1 | user1@example.com | password1 | USER |
| seller1 | seller1@example.com | password2 | SELLER |
| admin | admin@example.com | adminPass | ADMIN (all roles) |

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Register User
```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "string",
  "email": "string",
  "password": "string",
  "role": ["user", "seller", "admin"]
}
```

#### Login
```http
POST /api/auth/signin
Content-Type: application/json

{
  "username": "string",
  "password": "string"
}
```

#### Logout
```http
POST /api/auth/signout
```

#### Get Current User
```http
GET /api/auth/user
Authorization: Bearer {token}
```

#### Get All Sellers (Admin)
```http
GET /api/auth/sellers?pageNumber=0
Authorization: Bearer {token}
```

### Product Endpoints

#### Get All Products (Public)
```http
GET /api/public/products?pageNumber=0&pageSize=10&sortBy=productId&sortOrder=asc
```

#### Get Products by Category (Public)
```http
GET /api/public/categories/{categoryId}/products?pageNumber=0&pageSize=10
```

#### Search Products by Keyword (Public)
```http
GET /api/public/products/keyword/{keyword}?pageNumber=0&pageSize=10
```

#### Add Product (Admin)
```http
POST /api/admin/categories/{categoryId}/product
Authorization: Bearer {token}
Content-Type: application/json

{
  "productName": "string",
  "productDescription": "string",
  "quantity": 100,
  "price": 999.99,
  "discount": 10.0
}
```

#### Update Product (Admin)
```http
PUT /api/admin/products/{productId}
Authorization: Bearer {token}
Content-Type: application/json
```

#### Update Product Image
```http
PUT /api/products/{productId}/image
Authorization: Bearer {token}
Content-Type: multipart/form-data

image: [file]
```

#### Delete Product (Admin)
```http
DELETE /api/admin/products/{productId}
Authorization: Bearer {token}
```

### Category Endpoints

#### Get All Categories (Public)
```http
GET /api/public/categories?PageNumber=0&PageSize=10&sortBy=categoryId&sortOrder=asc
```

#### Create Category (Admin)
```http
POST /api/admin/categories
Authorization: Bearer {token}
Content-Type: application/json

{
  "categoryName": "string"
}
```

#### Update Category (Admin)
```http
PUT /api/admin/categories/{categoryId}
Authorization: Bearer {token}
Content-Type: application/json
```

#### Delete Category (Admin)
```http
DELETE /api/admin/categories/{categoryId}
Authorization: Bearer {token}
```

### Cart Endpoints

#### Create/Update Cart with Items
```http
POST /api/cart/create
Authorization: Bearer {token}
Content-Type: application/json

[
  {
    "productId": 1,
    "quantity": 2
  }
]
```

#### Add Product to Cart
```http
POST /api/carts/products/{productId}/quantity/{quantity}
Authorization: Bearer {token}
```

#### Get User's Cart
```http
GET /api/carts/users/cart
Authorization: Bearer {token}
```

#### Get All Carts (Admin)
```http
GET /api/carts
Authorization: Bearer {token}
```

#### Update Product Quantity in Cart
```http
PUT /api/cart/products/{productId}/quantity/{operation}
Authorization: Bearer {token}
```
Operation: "delete" for -1, any other value for +1

#### Remove Product from Cart
```http
DELETE /api/carts/{cartId}/product/{productId}
Authorization: Bearer {token}
```

### Order Endpoints

#### Place Order
```http
POST /api/order/users/payments/{paymentMethod}
Authorization: Bearer {token}
Content-Type: application/json

{
  "addressId": 1,
  "paymentMethod": "CARD",
  "pgName": "Stripe",
  "pgPaymentId": "pi_xxxxx",
  "pgStatus": "SUCCESS",
  "pgResponseMessage": "Payment completed"
}
```

#### Create Stripe Payment Intent
```http
POST /api/order/stripe-client-secret
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 100000,
  "currency": "usd",
  "email": "customer@example.com",
  "name": "Customer Name",
  "address": {
    "street": "123 Main St",
    "city": "City",
    "state": "State",
    "pincode": "12345",
    "country": "US"
  },
  "description": "Order payment",
  "metadata": {}
}
```

#### Get All Orders (Admin)
```http
GET /api/admin/orders?pageNumber=0&pageSize=10&sortBy=totalAmount&sortOrder=desc
Authorization: Bearer {token}
```

#### Get Seller Orders (Seller)
```http
GET /api/seller/orders?pageNumber=0&pageSize=10
Authorization: Bearer {token}
```

#### Update Order Status (Admin)
```http
PUT /api/admin/orders/{orderId}/status
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "SHIPPED"
}
```

#### Update Order Status (Seller)
```http
PUT /api/seller/orders/{orderId}/status
Authorization: Bearer {token}
Content-Type: application/json
```

### Address Endpoints

#### Create Address
```http
POST /api/addresses
Authorization: Bearer {token}
Content-Type: application/json

{
  "street": "string (min 5 chars)",
  "buildingName": "string (min 5 chars)",
  "city": "string (min 4 chars)",
  "state": "string (min 2 chars)",
  "country": "string (min 2 chars)",
  "pincode": "string (min 5 chars)"
}
```

#### Get All Addresses (Admin)
```http
GET /api/addresses
Authorization: Bearer {token}
```

#### Get Address by ID
```http
GET /api/addresses/{addressId}
Authorization: Bearer {token}
```

#### Get User's Addresses
```http
GET /api/addresses/users/addresses
Authorization: Bearer {token}
```

#### Update Address
```http
PUT /api/addresses/{addressId}
Authorization: Bearer {token}
Content-Type: application/json
```

#### Delete Address
```http
DELETE /api/addresses/{addressId}
Authorization: Bearer {token}
```

## Security

### JWT Authentication

The application uses JWT tokens stored in HTTP-only cookies for authentication:

1. User logs in with credentials
2. Server validates credentials
3. Server generates JWT token
4. Token is sent as HTTP-only cookie
5. Client includes cookie in subsequent requests
6. Server validates token on each request

### JWT Configuration

- **Token Expiration**: 24 hours (configurable)
- **Storage**: HTTP-only cookie
- **Path**: `/api`
- **Algorithm**: HMAC-SHA256

### Role-Based Access Control

| Role | Permissions |
|------|-------------|
| **USER** | View products, manage cart, place orders, manage addresses |
| **SELLER** | USER permissions + view own product orders |
| **ADMIN** | All permissions + manage products, categories, orders, users |

### Security Features

- Password encryption using BCrypt
- JWT token validation on protected endpoints
- Role-based endpoint protection
- CORS configuration
- CSRF protection disabled (stateless API)
- Session management: STATELESS

### Protected Endpoints

All endpoints under `/api` (except `/api/auth/**` and `/api/public/**`) require authentication.

## Database Schema

### Core Entities

#### User
- userId (PK)
- username (unique)
- email (unique)
- password (encrypted)
- roles (Many-to-Many with Role)
- addresses (One-to-Many with Address)
- products (One-to-Many with Product) - for sellers
- cart (One-to-One with Cart)

#### Product
- productId (PK)
- productName
- imageUrl
- productDescription
- quantity
- price
- discount
- specialPrice (calculated)
- category (Many-to-One with Category)
- user (Many-to-One with User) - seller
- cartItems (One-to-Many with CartItem)

#### Category
- categoryId (PK)
- categoryName
- products (One-to-Many with Product)

#### Cart
- cartId (PK)
- user (One-to-One with User)
- cartItems (One-to-Many with CartItem)
- totalAmount

#### CartItem
- cartItemId (PK)
- cart (Many-to-One with Cart)
- product (Many-to-One with Product)
- quantity
- discount
- productPrice

#### Order
- orderId (PK)
- email
- orderItems (One-to-Many with OrderItem)
- orderDate
- payment (One-to-One with Payment)
- totalAmount
- orderStatus
- address (Many-to-One with Address)

#### OrderItem
- orderItemId (PK)
- product (Many-to-One with Product)
- order (Many-to-One with Order)
- quantity
- discount
- orderedProductPrice

#### Payment
- paymentId (PK)
- order (One-to-One with Order)
- paymentMethod
- pgPaymentId (payment gateway)
- pgStatus
- pgResponseMessage
- pgName

#### Address
- addressId (PK)
- street
- buildingName
- city
- state
- country
- pincode
- user (Many-to-One with User)

#### Role
- roleId (PK)
- roleName (ROLE_USER, ROLE_SELLER, ROLE_ADMIN)

### Relationships

```
User 1---* Address
User 1---1 Cart
User *---* Role
User 1---* Product (as seller)
Cart 1---* CartItem
Product 1---* CartItem
Category 1---* Product
Order 1---* OrderItem
Order 1---1 Payment
Order *---1 Address
Product 1---* OrderItem
```

## Configuration

### Application Properties

Key configurations in `application.properties`:

```properties
# Database
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO}

# JWT
spring.app.jwtSecret=${SPRING_APP_JWTSECRET}
spring.app.jwtExpirationMs=${SPRING_APP_JWTEXPIRATIONMS}
spring.ecom.app.jwtCookieName=${SPRING_ECOM_APP_JWTCOOKIENAME}

# File Upload
project.image=${PROJECT_IMAGE}

# Stripe
stripe.secret.key=${STRIPE_SECRET_KEY}
```

### Constants

Default pagination and sorting values (in `AppConstants.java`):

```java
PAGE_NUMBER = "0"
PAGE_SIZE = "10"
SORT_CATEGORIES_BY = "categoryId"
SORT_PRODUCTS_BY = "productId"
SORT_DIR = "asc"
SORT_ORDERS_BY = "totalAmount"
SORT_USERS_BY = "userId"
```

## Error Handling

The application implements global exception handling:

### Custom Exceptions

1. **ResourceNotFoundException**: When requested resource not found
2. **APIException**: For business logic violations

### Error Response Format

```json
{
  "message": "Error message",
  "status": false
}
```

### Validation Errors

```json
{
  "fieldName1": "Error message 1",
  "fieldName2": "Error message 2"
}
```

## Business Logic Highlights

### Price Calculation
- Special Price = Price - (Discount% × Price)
- Cart Total = Σ(Product Special Price × Quantity)

### Order Processing
1. Validate cart has items
2. Create order with address
3. Process payment
4. Create order items from cart items
5. Update product quantities
6. Clear cart

### Cart Management
- Automatic cart creation on first product add
- Quantity validation against stock
- Real-time total calculation
- Prevents duplicate products

### Seller Order Filtering
Sellers only see orders containing their products through filtering logic in the service layer.

## Development Notes

### ModelMapper Usage
The application uses ModelMapper for DTO-Entity conversions, reducing boilerplate code.

### Transaction Management
`@Transactional` annotations ensure data consistency for operations like:
- Order placement
- Cart updates
- Address deletion

### Pagination Support
All list endpoints support pagination with configurable:
- Page number
- Page size
- Sort field
- Sort direction

### File Upload
Product images are stored locally with UUID-based filenames to prevent conflicts.

## Future Enhancements

Potential improvements:
- Review and rating system
- Wishlist functionality
- Email notifications
- Product recommendations
- Advanced search filters
- Inventory management
- Discount coupon system
- Return/refund management
- Analytics dashboard



## Support

For issues or questions, please contact [swadeshchatterjee512@gmail.com]
