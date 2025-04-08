FROM eclipse-temurin:21-jdk-jammy as build
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

# Create runtime image
FROM eclipse-temurin:21-jre-jammy as runtime
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar

# Add runtime configuration
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]