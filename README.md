# Solar System Monitoring

This project monitors solar system data and integrates with SolarEdge.

## Prerequisites
- Java 21 or higher
- Maven 3.8+
- (For production) Docker

## Project Structure
- `backend/` - Spring Boot backend
- `documentation/` - PRDs for project, backend, and frontend

## Running in Development Mode (dev)

1. **Navigate to the backend directory:**
   ```sh
   cd backend
   ```
2. **(Optional) Set environment variables for real SolarEdge API access:**
   - `SOLAREDGE_SITE_ID`
   - `SOLAREDGE_API_KEY`
   - `SOLAREDGE_API_URL`
   
   If you do not set these, the backend will return mock data in dev mode.

3. **Run the backend in dev mode:**
   - **Option 1: Regular Maven Command**
     ```sh
     ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
     ```
   - **Option 2: Windows Script**
     ```sh
     scripts\run-dev.bat
     ```
     This script will automatically run the application with the dev profile.

   The backend will be available at `http://localhost:8080` by default.
   Endpoints will return mock data if no API keys are set.

## Running in Production (Docker)

1. **Make sure you have Docker installed and running.**
2. **Make sure you are in the root directory of the application**
3. **Set environment variables for SolarEdge API access:**
   - `SOLAREDGE_SITE_ID`
   - `SOLAREDGE_API_KEY`
   - `SOLAREDGE_API_URL`
   
   You can set these in your shell or in the `docker-compose.yml` file.

4. **Build and run the containers:**
   ```sh
   docker-compose up --build
   ```
   - The backend will run in production mode and connect to the real SolarEdge API.

## API Documentation (Swagger / OpenAPI)

Interactive API documentation is available via Swagger UI.

- When the backend is running, open your browser and go to:
  - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  - or [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

You can use this interface to explore, test, and understand all available API endpoints.

## Documentation
- See the `documentation/` folder for PRDs:
  - `PRD.md` - Main project PRD
  - `PRD-BE.md` - Backend PRD
  - `PRD-FE.md` - Frontend PRD

---
For any issues or questions, please open an issue or contact the maintainer.
