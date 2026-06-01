# Enterprise Multi-Tenant POS System

## Overview

Enterprise Multi-Tenant POS (Point of Sale) System is a scalable SaaS-based application designed for billing, inventory management, customer management, and invoice processing. The system supports multi-tenancy, allowing multiple businesses to operate independently on the same platform with secure tenant-specific data isolation.

## Features

* Multi-Tenant Architecture
* Tenant-Specific Data Isolation
* JWT-Based Authentication & Authorization
* Role-Based Access Control
* Product Management
* Customer Management
* Inventory Tracking
* Invoice & Billing Management
* RESTful APIs
* Swagger API Documentation
* Responsive React Frontend

## Tech Stack

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* MySQL
* JWT Authentication

### Frontend

* React.js
* JavaScript
* HTML5
* CSS3

### Tools

* IntelliJ IDEA
* Postman
* Git
* GitHub
* Swagger UI

## Project Structure

backend/
├── src/main/java
├── src/main/resources
├── controller
├── service
├── repository
├── entity

frontend/
├── src
├── components
├── pages
├── services

## Installation

### Clone Repository

git clone https://github.com/your-username/enterprise-multi-tenant-pos-system.git

### Backend Setup

1. Configure MySQL database.
2. Update application.properties.
3. Run Spring Boot application.

### Frontend Setup

npm install

npm start

## API Documentation

Swagger UI:

http://localhost:8081/swagger-ui/index.html

## Database

MySQL database is used for storing:

* Users
* Roles
* Products
* Customers
* Invoices
* Tenant Information

## Future Enhancements

* Dashboard Analytics
* Payment Gateway Integration
* GST Invoice Generation
* Report Export (PDF/Excel)
* Docker Deployment
* Cloud Deployment (AWS/Azure)

## Author

Sakshi Dhamne

## License

This project is developed for learning, internship, and portfolio purposes.
