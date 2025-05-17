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
# On Windows:
mvnw.cmd spring-boot:run

# On Unix/Mac:
./mvnw spring-boot:run
```

3. Access the endpoints:

- Test endpoint: http://localhost:8080/test
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

### Option 2: Using Docker

From the project root directory:

```bash
# Build and start the application:
docker-compose up --build

# To run in detached mode (background):
docker-compose up -d

# To stop the application:
docker-compose down
```

## Running Tests

### Local Testing

```bash
cd backend
# On Windows:
mvnw.cmd test

# On Unix/Mac:
./mvnw test
```

### Docker Testing

```bash
# Run tests in Docker:
docker-compose up backend-test

# To stop after tests:
docker-compose down
```

## Environment Variables

The application supports different profiles:
- `default`: Used for local development
- `docker`: Used when running in Docker

You can set the profile using:
```bash
# For local development:
set SPRING_PROFILES_ACTIVE=default

# For Docker:
set SPRING_PROFILES_ACTIVE=docker
```
