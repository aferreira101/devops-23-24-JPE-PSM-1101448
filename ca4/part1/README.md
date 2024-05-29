# Technical Report: CA4, Part 1 - Containers with Docker

##### Made by: [André Ferreira - 1101448]

## Table of Contents

- [Tutorial](#tutorial)
    - [Step 1: Create a Dockerfile](#step-1-create-a-dockerfile)
    - [Step 2: Build the Docker Image](#step-2-build-the-docker-image)
    - [Step 3: Tag and Publish the Docker Image](#step-3-tag-and-publish-the-docker-image)
    - [Step 4: Run the Chat Server Container](#step-4-run-the-chat-server-container)
- [Conclusion](#conclusion)

## Tutorial

The goal of Part 1 of this assignment is to practice with Docker, creating docker images and running containers using
the chat application from CA2 **gradle basic demo**.

Before you start, make sure you have Docker installed on your machine.
Also, let's clone the repository to have access to the files and build the project using Gradle.

```bash 
    git clone https://bitbucket.org/pssmatos/gradle_basic_demo/
    cd gradle_basic_demo
    ./gradlew clean build
   ```

Ensure that the build is successful and the JAR file is created in the build/libs directory.

### Step 1: Create a Dockerfile

In the root of the repository, create a Dockerfile to define the Docker image. The Dockerfile should contain
instructions to build the chat server and run it.
Below are the contents of the Dockerfile for version 1.

```dockerfile
    # This Dockerfile is used to build and run a Java application using Gradle.
    # We start from a base image that includes Gradle and JDK 17.

    FROM gradle:jdk17 AS builder

    # Metadata indicating the authors of the image.

    LABEL authors="Andre Ferreira"

    # Set the working directory in the Docker image filesystem.
    
    WORKDIR /ca4-part1/
    
    # Clone the Gradle Basic Demo project from Bitbucket.
    
    RUN git clone https://bitbucket.org/pssmatos/gradle_basic_demo.git
    
    # Change the working directory to the cloned project directory.
    
    WORKDIR /ca4-part1/gradle_basic_demo
    
    # Make the Gradle wrapper executable and run the 'clean build' task.
    # This will clean the project, compile the code, and package it into a JAR file.
    
    RUN chmod +x gradlew && ./gradlew clean build
    
    # Expose port 59001 in the Docker container.
    # This is the port that our application will be accessible on.
    
    EXPOSE 59001
    
    # The command that will be run when the Docker container starts.
    # It starts the Java application and the Gradle server.
    
    CMD ["sh", "-c", "java -jar build/libs/*.jar & gradle runServer"]
   ```

Below are the contents of the Dockerfile for version 2.

```dockerfile
    # This Dockerfile is used to build an image for a Java application.
    # We start from a base image that already has Java installed.
    # openjdk:17-jdk-slim is a slim version of the OpenJDK 17 image.

    FROM openjdk:17-jdk-slim

    # We set the working directory inside the container to /ca4-part1/.
    # All subsequent commands will be run from this directory.

    WORKDIR /ca4-part1/

    # We copy the built JAR file from our host machine to the container.
    # The JAR file is located in the build/libs directory of the gradle_basic_demo project.
    # It is named ca4-part1.jar inside the container.

    COPY ./gradle_basic_demo/build/libs/*.jar ca4-part1.jar

    # We expose port 59001 in the container.
    # This is the port that our application will be accessible on.

    EXPOSE 59001

    # We define the command that will be run when the container is started.
    # The application is started with the java command, using the -cp option to specify the classpath.
    # The main class of the application is basic_demo.ChatServerApp and it listens on port 59001.

    ENTRYPOINT ["java", "-cp", "ca4-part1.jar", "basic_demo.ChatServerApp", "59001"]
   ```

### Step 2: Build the Docker Image

Build the Docker image using the Dockerfile. Specify the Dockerfile to use with the -f flag.

   ```bash 
    docker build -f Dockerfile_v1 -t chat-server:latest .
   ```

### Step 3: Tag and Publish the Docker Image

1. Tag the Docker image with your Docker Hub repository name. Replace your-dockerhub-username with your actual Docker
   Hub username.

    ```bash 
            docker tag chat-server:latest your-dockerhub-username/chat-server:latest
       ```

2. Push the Docker image to Docker Hub.

     ```bash 
            docker login
            docker push your-dockerhub-username/chat-server:latest
       ```

### Step 4: Run the Chat Server Container

Run the Docker container from the pushed image. This will start the chat server inside the container.

```bash
  docker run -p 59001:59001 your-dockerhub-username/chat-server:latest
   ```

## Conclusion

In this tutorial, we created a Docker image for the chat server application from CA2 and ran it inside a Docker
container.
We used a multi-stage build process to optimize the image size and reduce the attack surface.
We also published the Docker image to Docker Hub to share it with others.

[Back to Top](#table-of-contents)

# END OF README

```