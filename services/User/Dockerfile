FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Set Java module opens for Lombok compatibility
ENV MAVEN_OPTS="--add-opens jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED --add-opens jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED"

# Copy Maven files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application with Lombok fixes
RUN ./mvnw clean package -DskipTests

# Create final image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8090

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8090/api/actuator/health || exit 1

# Run the application with Lombok agent
ENTRYPOINT ["java", "-javaagent:app.jar", "-jar", "app.jar"]