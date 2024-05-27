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
    # Dockerfile_v1 is used to build the gradle_basic_demo project in a Docker container.
    # The first stage of the build process. We're using the gradle:jdk21 image as our base.
    
    FROM gradle:jdk21 AS builder
    
    # Metadata for the image. In this case, it's the author of the Dockerfile_v1.
    
    LABEL authors="Andre Ferreira"
    
    # Sets the working directory in the Docker image.
    # This is where all the commands will be run.
    
    WORKDIR /ca4-part1/
    
    # Clones the gradle_basic_demo repository from Bitbucket into the Docker image.
    # This is the source code that will be built.
    
    RUN git clone https://bitbucket.org/pssmatos/gradle_basic_demo.git
    
    # Changes the working directory to the cloned repository.
    # This is where the build commands will be run.
    
    WORKDIR /ca4-part1/gradle_basic_demo
    
    # Makes the Gradle wrapper script executable and runs the Gradle clean and build tasks inside the Docker image.
    # This builds the source code into a runnable application.
    
    RUN chmod +x gradlew && ./gradlew clean build
    
    # Exposes port 59001 on the Docker container.
    # This is the port that the application will be accessible on.
    
    EXPOSE 59001
    
    # The command that will be run when the Docker container is started.
    # This starts the application.
    
    CMD ["sh", "-c", "java -jar build/libs/*.jar & gradle runServer"]
    ```

Below are the contents of the Dockerfile for version 2.

    ```dockerfile
    # This Dockerfile_v2 is used to build a Docker image for the ChatServerApp application.
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
    docker run -d -p 59001:59001 your-dockerhub-username/chat-server:latest
    ```

## Conclusion

In this tutorial, we created a Docker image for the chat server application from CA2 and ran it inside a Docker
container.
We used a multi-stage build process to optimize the image size and reduce the attack surface.
We also published the Docker image to Docker Hub to share it with others.

[Back to Top](#table-of-contents)

# END OF README

```