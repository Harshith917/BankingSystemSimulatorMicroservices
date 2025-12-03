Banking System Simulator — Microservices Architecture

Java · Spring Boot · Spring Cloud · Docker · Resilience4J · Eureka · API Gateway

A production-style, container-ready Spring Boot Microservices Banking System using:

Spring Cloud Eureka (Service Registry)

Spring Cloud Gateway (API Gateway)


Feign Clients with Circuit Breaker + Fallback

Resilience4J for fault tolerance

Docker Compose for full containerization

Independent microservices for Accounts, Transactions, and Notifications

MongoDB per service

Centralized Config Server

📌 Table of Contents

Overview

Architecture

Project Structure

Features

Tech Stack

Setup (Local)

Running with Docker

Service URLs

API Endpoints

Failure Handling (Circuit Breaker Demo)

Future Enhancements

🧩 Overview

This project simulates a real-world banking backend built using Spring Cloud Microservices.

It demonstrates:

Account creation & balance management

Deposits, withdrawals, and transfers

Distributed transaction flow

Service discovery with Eureka

Routing via API Gateway

Fault-tolerant communication using Feign + Resilience4J

Independent MongoDB databases

Fully containerized architecture

When notification-service is DOWN,
➡ transaction-service still works using fallback.
➡ No customer-facing failure — ensures banking resilience.

🏛 Architecture
                   
<img width="1114" height="747" alt="Screenshot 2025-12-04 013153" src="https://github.com/user-attachments/assets/0d533923-6ba7-40e5-8420-a2be4b481101" />




📁 Project Structure
<img width="304" height="330" alt="image" src="https://github.com/user-attachments/assets/216c5b4f-325c-4c5c-86ff-ff28ab0de621" />


⭐ Features
✔ Account Service

Create account

Fetch account details

Update balance

✔ Transaction Service

Deposit

Withdraw

Transfer

Transaction history

Safe notification with Resilience4J fallback

✔ Notification Service

Simulated email notifications

Automatically skipped when down (fallback)

✔ API Gateway

Single entry point

Eureka-based dynamic routing

✔ Eureka Server

Dynamic service registry

Shows availability of all microservices

✔ Resilience4J Circuit Breaker

Protects transaction-service from notification-service failure

Ensures banking operations always succeed

🛠 Tech Stack
Component	Technology
Language	Java 17
Framework	Spring Boot 3.x
Discovery	Spring Cloud Netflix Eureka
Gateway	Spring Cloud Gateway
Fault Tolerance	Resilience4J
Inter-service Calls	OpenFeign
Databases	MongoDB
Build Tool	Maven
Containerization	Docker, Docker Compose
Architecture	Microservices
🚀 Setup (Local Run)
1️⃣ Clone the Repository
git clone <your-repo-url>
cd banking-microservices

2️⃣ Build All Microservices
mvn clean install -DskipTests

3️⃣ Start Services in Order

config-server

eureka-server

account-service

notification-service

transaction-service

api-gateway

🐳 Running With Docker
docker-compose up --build


This automatically launches:

Config Server

Eureka Server

API Gateway

Account Service

Transaction Service

Notification Service

MongoDB containers

🌐 Service URLs
Service	URL
API Gateway	http://localhost:8080

Eureka Dashboard	http://localhost:8761

Config Server	http://localhost:8888

Account Service	Registered via Eureka
Transaction Service	Registered via Eureka
Notification Service	Registered via Eureka

Swagger can be added if you enable Springdoc:

http://localhost:<port>/swagger-ui/index.html

📡 API Endpoints (via Gateway)
1️⃣ Create Account
POST /api/accounts
{
  "accountNumber": "{accountNumber}",
  "holderName": "{holderName}",
  "balance": {balance}
}

2️⃣ Deposit
POST /api/transactions/{accountNumber}/deposit
{
  "amount": {amount}
}

3️⃣ Withdraw
POST /api/transactions/{accountNumber}/withdraw
{
  "amount": {amount}
}

4️⃣ Transfer
POST /api/transactions/transfer
{
  "sourceAccount": "{sourceAccount}",
  "destinationAccount": "{destinationAccount}",
  "amount": {amount}
}

5️⃣ Get Account Details
GET /api/accounts/{accountNumber}

🛡 Failure Handling (Circuit Breaker Demo)
Scenario: Notification Service is DOWN

➡ Transactions should still succeed
➡ Notification should fallback gracefully

Run:

docker-compose stop notification-service


Then deposit:

POST /api/transactions/{accountNumber}/deposit
{
  "amount": 100
}

Expected Logs:
⚠ Notification failed because: FeignException$ServiceUnavailable
Skipping notification. Request: NotificationRequest(email=..., message=...)

Expected Behavior:
Component	Behavior
Transaction Service	Works normally
Account balance update	Works normally
Notification	Skipped (fallback)
User	Gets SUCCESS response

This proves fault isolation & resilience.

🚧 Future Enhancements

JWT Authentication

Centralized Logging (ELK / OpenTelemetry)

API Rate Limiting (Redis)

Loan/FD/RD Microservices

Swagger for all services

Kafka event-driven notifications
