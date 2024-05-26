# This Dockerfile is used to build and run a Java application in a Docker container.

# The first stage of the build process. We're using the gradle:jdk17 image as our base.
FROM gradle:jdk17 AS builder

# Metadata for the image. In this case, it's the author of the Dockerfile.
LABEL authors="Andre Ferreira"

# Sets the working directory in the Docker image.
WORKDIR /ca4-part1/

# Clones the gradle_basic_demo repository from Bitbucket into the Docker image.
RUN git clone https://bitbucket.org/pssmatos/gradle_basic_demo.git

# Changes the working directory to the cloned repository.
WORKDIR /ca4-part1/gradle_basic_demo

# Makes the Gradle wrapper script executable and runs the Gradle clean and build tasks inside the Docker image.
RUN chmod +x gradlew && ./gradlew clean build

# The second stage of the build process. We're using the openjdk:17-jdk-slim image as our base.
FROM openjdk:17-jdk-slim

# Sets the working directory in the Docker image.
WORKDIR /ca4-part1/

# Copies the built JAR file from the previous build stage to the current Docker image.
COPY --from=builder /ca4-part1/gradle_basic_demo/build/libs/*.jar ca4-part1.jar

# Configures the Docker container to run the Java application when it's started.
# The ENTRYPOINT instruction allows you to configure a container that will run as an executable.
EXPOSE 59001
ENTRYPOINT ["java", "-cp", "ca4-part1.jar", "basic_demo.ChatServerApp", "59001"]