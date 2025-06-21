# Get JAVA JDK for compiling, development and testing
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace/app

# Copy only the files needed for dependency resolution+
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies first (better cache utilization)
RUN ./mvnw dependency:go-offline

# Then copy source code and build
COPY src src
RUN ./mvnw package -DskipTests

# Create runtime image (only JRE)
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar

# Add non-root user - security, limit access to the container resouces
RUN useradd -u 1000 appuser
USER appuser

ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]