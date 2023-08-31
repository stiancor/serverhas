# Start from a base image with Java
FROM openjdk:17.0.1-jdk-slim

# Set the current working directory inside the container
WORKDIR /app

# Copy the JAR file to the working directory
COPY ./app/build/libs/app-all.jar /app/app.jar

# Set the default command to run your application
CMD ["java", "-jar", "/app/app.jar"]