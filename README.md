# Crypto Ranking App Backend

## Overview
The Crypto Ranking App Backend is a Spring Boot application that provides services for fetching and storing cryptocurrency data. It integrates with external APIs to fetch coin values and stores the data in a Redis database for efficient retrieval. The application also includes user authentication and authorization features.

## Features
- Fetch and store cryptocurrency data from external APIs
- Store coin history in Redis
- Scheduled tasks for syncing recent, medium-term, and long-term data
- Rate limiting during data synchronization
- Initialization checks for coin history in Redis
- Logging for initialization and data sync processes
- User registration, login, and email verification
- Password reset functionality
- JWT-based authentication and authorization

## Technologies Used
- Java
- Spring Boot
- Maven
- Redis
- PostgreSQL
- Spring Security
- JWT

## Configuration
The application properties are configured in `application.properties`:
- `spring.application.name=coinRank`
- `server.port=9001`
- `management.endpoint.health.show-details=always`
- `server.servlet.context-path=/coinRank`
- Redis URL
- API host and key for coin ranking
- Cron job schedule for coin API
- PostgreSQL datasource with driver, URL, username, password, and pool size
- JWT secret and expiration settings
- Spring Mail with SMTP settings

## Scheduled Tasks
- `syncMediumTermData`: Syncs medium-term data every 6 hours.
- `syncLongTermData`: Syncs long-term data once a day at midnight.

## API Endpoints
- `/coins`: Retrieves all coins.
- `/coins/history`: Retrieves coin history for a given symbol and time period.
- `/api/v1/user/register`: Registers a new user.
- `/api/v1/user/verify-email`: Verifies the user's email.
- `/api/v1/user/login`: Logs in a user.
- `/api/v1/user/forgot-password`: Initiates the forgot password process.
- `/api/v1/user/reset-password`: Resets the user's password.

## Running the Application
1. Clone the repository.
2. Configure the application properties in `application.properties`.
3. Build the project using Maven:
   ```sh
   mvn clean install