# Build stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace/app

# Copy Maven wrapper and POM first for better caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Ensure Maven wrapper is executable
RUN chmod +x mvnw
# Download dependencies (this layer will be cached)
RUN ./mvnw dependency:go-offline

# Copy source code and build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar

# Set Java options for containerized environment
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]