# Slideshow Management Application

## Overview

The Slideshow Management Application provides an API for managing slideshows and images. This includes CRUD operations for slides, slideshows, and associated metadata. The application allows functionalities such as adding/removing images, searching slideshows, managing slideshow orders, and logging proof-of-play events.

This project is built with Spring WebFlux, R2DBC, and MySQL, and comes with a Swagger UI for interactive API documentation.

## Features

- **Add/Remove Images**
- **Create/Delete Slideshows**
- **Manage Slideshow Order**
- **Proof of Play Event Logging**
- **Search Slideshows**

## Prerequisites

Before running the application, ensure you have the following installed:

- [Docker](https://www.docker.com/get-started) (for database and broker setup)
- [JDK 17 or later](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Setup

### 1. Clone the Repository

Clone this repository to your local machine:

```bash
git clone https://github.com/NazDov/slideshow-management.git
cd slideshow-management
```

###  2. Start Docker Services

Run the following command to start the required services (MySQL and Kafka):

```bash
docker-compose up -d
```
### 3. Build and Run the Application
   After the Docker services are up, build and run the Spring Boot application with a script:

```bash
chmod +x ./build_and_run.sh
```
```bash
./build_and_run.sh
```
The script will build the application and start it.
Once the application is running, it will be accessible at http://localhost:8080.

### API Documentation (Swagger)
Once the application is running, you can access the Swagger UI for interactive API documentation at:

http://localhost:8080/swagger-ui/index.html

The Swagger UI provides detailed information about each endpoint, request/response models, and allows you to try out the API directly from your browser.


