# Use OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file into the container
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
