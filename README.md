# Solar System Monitoring

A monitoring system for solar installations using Spring Boot backend.

## Prerequisites

- Java Development Kit (JDK) 21
- Maven (included via wrapper)
- Docker Desktop (optional, for containerized running)
- Git

## Quick Start

1. Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/solar-system-monitoring.git
cd solar-system-monitoring
```

2. Choose your preferred way to run:

### Local Development
Navigate to backend directory and run:
```bash
cd backend
mvnw.cmd spring-boot:run  # On Windows
./mvnw spring-boot:run    # On Unix/Mac
```

### Docker Deployment
From the project root directory (no need to cd into backend):
```bash
docker-compose up --build
```

## Documentation

- Backend API (when running): http://localhost:8080/swagger-ui.html
- Test endpoint: http://localhost:8080/test
- API Documentation: http://localhost:8080/api-docs

For more detailed instructions, see `backend/README.md`
