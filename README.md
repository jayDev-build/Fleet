# Fleet Management System (Fleet API)

A production-ready Spring Boot backend application designed to streamline logistics and fleet operations. It enables fleet operators to manage vehicles, drivers, trips, expenses, vehicle owners, and financial settlements, including automated WhatsApp notification alerts via Meta Cloud APIs.

---

## Table of Contents
1. [Key Features](#key-features)
2. [Tech Stack](#tech-stack)
3. [Architecture & Packages](#architecture--packages)
4. [Prerequisites](#prerequisites)
5. [Database Schema & Core Entities](#database-schema--core-entities)
6. [Configuration & Environment Setup](#configuration--environment-setup)
7. [Getting Started (Run Locally)](#getting-started-run-locally)
8. [API Endpoints Directory](#api-endpoints-directory)
   - [Authentication](#1-authentication)
   - [User / Dashboard](#2-user--dashboard)
   - [Vehicle Owners](#3-vehicle-owners)
   - [Drivers](#4-drivers)
   - [Vehicles](#5-vehicles)
   - [Trips](#6-trips)
   - [Expenses](#7-expenses)
   - [Transactions](#8-transactions)
9. [External Integrations](#external-integrations)
10. [Testing with Postman](#testing-with-postman)

---

## Key Features
- **Fleet Operators / Users Control**: Secure JWT-based registration, authentication, and user profile management.
- **Unified Fleet Dashboard**: At-a-glance analytics showing total trips, freight revenues, grouped expenses (diesel, toll, driver, other), owner payouts, and net profit.
- **Vehicle Ownership Model**: Lease management with third-party vehicle owners, tracking vehicle-to-owner mappings and rental calculations.
- **Trip Lifecycle Management**: Creation of trips with source, destination, designated vehicle, driver, status transitions (`CREATED` $\rightarrow$ `ACTIVE` $\rightarrow$ `COMPLETED`), and financial settlement toggles.
- **Expense Bookkeeping**: Add, update, view, and delete trip-related expenses categorized into Diesel, Toll, Driver, and Other expenses.
- **Owner Transaction Ledger**: Double-entry ledger of transactions between fleet operators and vehicle owners to track advances and pending balances.
- **Messaging & Notifications**:
  - Outbound WhatsApp notifications via **Meta Cloud API** and **Twilio**.
  - Built-in cron scheduler reminders for logging daily expenses.

---

## Tech Stack
- **Framework**: Spring Boot 4.0.x (with Spring Web MVC, Spring Security, Spring Data JPA)
- **Language**: Java 21
- **Database**: MySQL
- **ORM**: Hibernate
- **Build Tool**: Maven
- **External APIs**: Meta Cloud WhatsApp API, Twilio SDK
- **Utilities**: Lombok, JWT (jjwt)

---

## Architecture & Packages
The project adheres to standard Spring Boot layered architecture guidelines:
- **`Controllers`**: REST API endpoints and payload validation.
- **`Dto`**: Request and response Transfer Objects separating internal models from API contracts.
- **`Models`**: JPA Entity classes representing database tables.
- **`Repository`**: Spring Data JPA repositories interfacing with MySQL.
- **`Service`**: Core business workflows, computation rules, and database transactions.
- **`Security`**: JWT filters, user details services, password encoders, and resource authorization configurations.
- **`Enums`**: Shared status states, payment methods, and expense categories (`Status`, `Method`, `ExpenseType`).
- **`Jobs`**: Background scheduler tasks (e.g. daily expense reminder triggers).
- **`Whatsapp`**: Infrastructure components for calling Meta Cloud / Graph APIs.

---

## Prerequisites
Before running the application, ensure you have:
- **Java Development Kit (JDK)**: Version 21 or higher
- **Maven**: Version 3.8+ (or use the included wrapper `./mvnw`)
- **MySQL**: Running local instance or cloud hosting (e.g. Railway, RDS)

---

## Database Schema & Core Entities

The system defines the following key relational entities:
- **User**: Represents the Fleet Manager account with login credentials.
- **Owner**: Person/company owning leased trucks. Maps $1 \rightarrow N$ with vehicles.
- **Vehicle**: Truck identifiers (e.g. registration numbers) leased from Owners and managed by Users.
- **Driver**: Active drivers with contact details.
- **Trip**: Connects a User, Vehicle, and Driver for a logistics route. Calculates profit, freight rate, and owner rate.
- **Expense**: Linked to trips or vehicles, tracking diesel, toll, driver allowance, and custom categories.
- **Transactions**: Tracks payouts, advances, and adjustments between the Fleet Manager and Owners.

---

## Configuration & Environment Setup

Configure database connections and external API credentials in the environment. Create a `.env` file in the project's root folder with placeholder parameters below:

```properties
# Database Configuration
DATASOURCE_URL=jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC
DATASOURCE_USERNAME=your_database_username
DATASOURCE_PASSWORD=your_database_password

# Spring Security Config
JWT_SECRET_KEY=your_base64_encoded_jwt_secret_key_of_at_least_256_bits_length

# Twilio API Integration
TWILIO_ACCOUNT_SID=your_twilio_account_sid
TWILIO_AUTH_TOKEN=your_twilio_auth_token
TWILIO_WHATSAPP_FROM=whatsapp:+your_twilio_whatsapp_number

# Frontend Redirection (CORS Configuration)
FRONTEND_URL=http://localhost:5173

# Server Port (Defaults to 8080)
PORT=8080

# Meta Cloud WhatsApp API Configs
WHATSAPP_API_URL=https://graph.facebook.com/v21.0
WHATSAPP_API_PHONE_NUMBER_ID=your_whatsapp_phone_number_id
WHATSAPP_API_ACCESS_TOKEN=your_meta_system_user_access_token
```

*Note: Avoid committing your actual `.env` file or passwords to version control. The application loads the variables into `src/main/resources/application.properties` dynamically.*

---

## Getting Started (Run Locally)

1. **Database Setup**: Ensure MySQL is running. Create a schema matching the database name specified in `DATASOURCE_URL`.
2. **Build the Application**: Compile classes and package the jar via Maven:
   ```bash
   mvn clean install
   ```
3. **Start the App**: Run using the Maven plugin or running the main class:
   ```bash
   mvn spring-boot:run
   ```
   Alternatively, execute the packaged JAR:
   ```bash
   java -jar target/Fleet-0.0.1-SNAPSHOT.jar
   ```
4. **Access Swagger/API**: The default API base context-path is `/api/v1`. If running on port `8080`, check `http://localhost:8080/api/v1`.

---

## API Endpoints Directory

All endpoints (except auth routes) require authentication. Pass the JWT token inside the request header as `Authorization: Bearer <your-jwt-token>`.

### 1. Authentication
Endpoints managing user access. Authentication is public (`/auth/**`).

- **Sign Up**
  - `POST /auth/signup`
  - Body: `SignUpRequestDto` (username, password)
  - Returns: HTTP 201 with created details.
- **Log In**
  - `POST /auth/login`
  - Body: `LoginRequestDto` (username, password)
  - Returns: HTTP 200 with access JWT token and userId.
- **Verify Session**
  - `GET /auth/verify`
  - Returns: HTTP 200 if token is active/valid.

### 2. User / Dashboard
Retrieve manager statistics and edit user meta records.

- **Update Profile**
  - `POST /user/profile/update`
  - Body: `UserRequestDto` (name, phone)
- **Get Profile Details**
  - `GET /user/profile`
- **Dashboard Summary Stats**
  - `GET /user/dashboard`
  - Returns: Aggregated financial dashboard (Total trips, freight prices, overall expenses, profit).

### 3. Vehicle Owners
Register and query third-party truck owners.

- **Create Owner**
  - `POST /owner/`
  - Body: `OwnerRequestDto` (name, phone)
- **Get All Owners**
  - `GET /owner/`
- **Get Specific Owner**
  - `GET /owner/{ownerId}`
- **Get Owner Financial Balance**
  - `GET /owner/{ownerId}/balance`

### 4. Drivers
Add and read active driver profiles.

- **Create Driver**
  - `POST /driver/`
  - Body: `DriverRequestDto` (name, phone)
- **Get All Drivers**
  - `GET /driver/`
- **Get Driver Details**
  - `GET /driver/{driverId}`
- **Get Driver Dropdown List**
  - `GET /driver/drivers` (returns light list of ID and Name pairs)

### 5. Vehicles
Register, lookup, and track individual vehicles.

- **Create Vehicle**
  - `POST /vehicle/`
  - Body: `VehicleRequestDto` (vehicleNumber, ownerId)
- **Get All Vehicles**
  - `GET /vehicle/`
- **Get Vehicle By Registration Number**
  - `GET /vehicle/number/{number}`
- **Get Vehicle By ID**
  - `GET /vehicle/{id}`
- **Update Vehicle Info**
  - `PATCH /vehicle/{vehicleId}`
- **Get Vehicle Dropdown List**
  - `GET /vehicle/vehicles` (returns light list of ID and Vehicle Number pairs)

### 6. Trips
Manage logistics schedules, costs, and progress.

- **Create Trip**
  - `POST /trip/`
  - Body: `TripRequestDto` (driverId, vehicleId, source, destination, freightPrice, ownerRate, startDate)
- **Get All Trips**
  - `GET /trip/`
- **Get Detailed Trip**
  - `GET /trip/{tripId}`
- **Get Trip Calculation Summary**
  - `GET /trip/summary/{tripId}`
- **Get Trip Status**
  - `GET /trip/status/{tripId}`
- **Change Status to Active (Start Trip)**
  - `PATCH /trip/status/{tripId}/start`
- **Change Status to Completed (Close Trip)**
  - `PATCH /trip/status/{tripId}/close`
- **Settle Trip Balance**
  - `PATCH /trip/{tripId}/settle`

### 7. Expenses
Track single logs for fuel, tolls, or driver allowances.

- **Create Expense**
  - `POST /expense/`
  - Body: `ExpenseRequestDto` (amount, expenseType, date, description, tripId)
- **Get Expense Details**
  - `GET /expense/{expenseId}`
- **Get Trip Expenses**
  - `GET /expense/trip/{tripId}`
- **Update Expense**
  - `PATCH /expense/trip/{expenseId}`
  - Body: `UpdateExpenseRequestDto` (amount, description, expenseType, date)
- **Delete Expense**
  - `DELETE /expense/{expenseId}`

### 8. Transactions
Post manual advances or payments to vehicle owners.

- **Record Transaction**
  - `POST /transactions/`
  - Body: `TransactionRequestDto` (amount, method, description, date, ownerId)
- **Get Ledger with Owner**
  - `GET /transactions/{ownerId}`

---

## External Integrations

### WhatsApp Messages (Meta Cloud API)
The application leverages the Meta Graph infrastructure to broadcast templated notifications to drivers or fleet managers.
- Settings are specified in variables prefixed with `whatsapp.api`.
- Programmatic payloads are formed using `WhatsAppPayload` and POSTed dynamically via Spring's asynchronous RestClient.

### Expense logging schedulers
- A background scheduler is built in `ExpenseReminderScheduler.java`.
- It scans the active trip list and triggers notifications if no expenses have been logged for a vehicle within 24 hours.

---

## Testing with Postman

Import the provided `Fleet.postman_collection.json` directly into your Postman Workspace to test authorization, registration, CRUD requests, status transitions, and owner financial calculations instantly. Ensure your local server is running on `http://localhost:8080` before executing requests.
