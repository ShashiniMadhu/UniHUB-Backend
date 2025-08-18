# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Install necessary packages
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

# Copy Maven wrapper and pom.xml first (for better caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Create necessary directories
RUN mkdir -p logs && mkdir -p uploads

# Expose the port your app runs on
EXPOSE 8086

# Run the application
CMD ["java", "-jar", "target/Server-root-0.0.1-SNAPSHOT.jar"]