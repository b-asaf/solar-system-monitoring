# Solar System Monitoring - Backend

Spring Boot backend service for the Solar System Monitoring project.

## Running the Application

### Option 1: Local Development

1. Navigate to backend directory:

```bash
cd backend
```

2. Start the application:

```bash
./mvnw spring-boot:run
```

3. Access the endpoints:

- Main API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

### Option 2: Using Docker

From the project root directory:

```bash
docker-compose up backend
```

## Running Tests

### Local Testing

```bash
cd backend
./mvnw test
```

### Docker Testing

```bash
docker-compose up backend-test
```
