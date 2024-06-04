# Technical Report: CA4, Part 2 - Containers with Docker

###### Made by: [André Ferreira - 1101448]

## Table of Contents

- [Tutorial](#tutorial)
    - [Step 1: Create the Dockerfiles](#step-1-create-the-dockerfiles)
        - [Web Application Dockerfile](#web-application-dockerfile)
        - [Database Dockerfile](#database-dockerfile)
        - [Docker Compose File](#docker-compose-file)
    - [Step 2: Build the Docker Images](#step-2-build-the-docker-images)
    - [Step 3: Publish the Docker Images](#step-3-publish-the-docker-images)
- [Conclusion](#conclusion)
- [Exploring Kubernetes: An Alternative to Docker](#exploring-kubernetes-an-alternative-to-docker)
    - [Comparing Kubernetes to Docker](#comparing-kubernetes-to-docker)
    - [Using Kubernetes for the Same Goals](#using-kubernetes-for-the-same-goals)
    - [Conclusion](#conclusion-1)

## Tutorial

The goal of Part 2 of this assignment is to practice with Docker, creating two images and a docker-compose file
to run those images in a containerized environment using the **react-and-spring-data-rest-basic** from CA2.

Before you start, make sure you have Docker installed on your machine,
and copy the **react-and-spring-data-rest-basic** project from CA2 to the root of the repository.

```bash
    cp -r ../ca2/react-and-spring-data-rest-basic .
```

### Step 1: Create the Dockerfiles

In the root of the repository, create two Dockerfiles, one for creating an image that runs the application and another
for the database. The Dockerfiles should contain instructions to build the application and database images.
Then, create a docker-compose file to run the images in a containerized environment. Below are the contents of the
Dockerfiles.

#### Web Application Dockerfile

This Dockerfile is used to create a Docker image for the **react-and-spring-data-rest-basic** application.

```dockerfile
# Use openjdk:17-jdk-slim as a base image for the builder stage
FROM openjdk:17-jdk-slim as builder

# Set the working directory in the container to /ca4-part2
WORKDIR /ca4-part2

# Copy the local directory ./react-and-spring-data-rest-basic into the container
COPY ./react-and-spring-data-rest-basic .

# Change the permissions of the gradlew file to make it executable
RUN chmod +x gradlew

# Execute the gradlew build command
CMD ["./gradlew", "build"]

# Expose port 8080 to the host machine
EXPOSE 8080

# Run the jar file created by the build process
CMD ["java", "-jar", "build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.jar"]
```

#### Database Dockerfile

This Dockerfile is used to create a Docker image for an H2 database server.

```dockerfile
# This Dockerfile is used to create a Docker image for an H2 database server.

# We start from an Ubuntu 20.04 (Focal Fossa) base image.
FROM ubuntu:focal

# Update the package lists for upgrades and new package installations.
RUN apt-get update && apt-get upgrade -y

# Fix broken dependencies.
RUN apt-get install -f

# Install wget and OpenJDK 17 without GUI components.
RUN apt-get install -y wget openjdk-17-jdk-headless

# Clean up the package lists to free up space.
RUN rm -rf /var/lib/apt/lists/*

# Set the working directory to /opt/h2.
WORKDIR /opt/h2

# Download the H2 database jar file from the Maven repository.
RUN wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar -O h2.jar

# Create a directory for the H2 database data files.
RUN mkdir -p /opt/h2-data

# Create a volume for the H2 database data files.
VOLUME /opt/h2-data

# Expose port 8082 for the H2 console.
EXPOSE 8082

# Expose port 9092 for TCP connections to the H2 database.
EXPOSE 9092

# The command to start the H2 database server.
CMD ["java", "-cp", "h2.jar", "org.h2.tools.Server", "-ifNotExists", "-web", "-webAllowOthers", "-webPort", "8082", "-tcp", "-tcpAllowOthers", "-tcpPort", "9092"]
```

#### Docker Compose File

This docker-compose file is used to run the two Docker images in a containerized environment.

```yaml
services:
  db:
    build:
      context: .
      dockerfile: Dockerfile_db
    container_name: CA4_Part2_db
    ports:
      - "8082:8082"
      - "9092:9092"
    volumes:
      - h2-data:/opt/h2-data
    networks:
      CA4_Part2_default:
        ipv4_address: 192.168.56.11

  web:
    build:
      context: .
      dockerfile: Dockerfile_web
    container_name: CA4_Part2_web
    ports:
      - "8080:8080"
    networks:
      CA4_Part2_default:
        ipv4_address: 192.168.56.10
    depends_on:
      - "db"

networks:
  CA4_Part2_default:
    ipam:
      driver: default
      config:
        - subnet: 192.168.56.0/24

volumes:
  h2-data:
    driver: local
```

### Step 2: Build the Docker Images

To build and run the Docker images, use the following commands:

```bash
docker-compose up --build
```

### Step 3: Publish the Docker Images

Tag the Docker images with your Docker Hub repository name and push them to Docker Hub.

```bash
docker tag part2-web 1101448/part2-web
docker tag part2-db 1101448/part2-db
docker push 1101448/part2-web
docker push 1101448/part2-db
```

## Conclusion

In this part of the assignment, we created two Docker images and a docker-compose file to run the images in a
containerized environment. The Docker images were built using Dockerfiles that contained instructions to build the
application and database images. The docker-compose file was used to run the images in a containerized environment. The
application image ran the **react-and-spring-data-rest-basic** application, while the database image ran an H2 database
server. The docker-compose file defined the services for the application and database images, as well as the network
configuration and volumes. The images were built and run using the `docker-compose up --build` command, and the images
were tagged and pushed to Docker Hub using the `docker tag` and `docker push` commands.

## Exploring Kubernetes: An Alternative to Docker

**Kubernetes** is an open-source platform designed to automate deploying, scaling, and operating containerized
applications.
While Docker focuses on individual containerization, Kubernetes provides orchestration for managing multiple containers
at scale.

### Comparing Kubernetes to Docker

| Feature                                 | Docker                                                                                                            | Kubernetes                                                                                                                 |
|-----------------------------------------|-------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| Basic Containerization                  | Focuses on the creation, deployment, and management of individual containers.                                     | Does not handle container creation directly; instead, it orchestrates containers created by Docker.                        |
| Orchestration                           | Docker Swarm is Docker’s native clustering and orchestration tool, but it’s less powerful compared to Kubernetes. | Offers powerful orchestration capabilities including automatic scaling, self-healing, load balancing, and rolling updates. |
| Scaling                                 | Manual scaling or using Docker Swarm for automatic scaling.                                                       | Automated scaling with the Horizontal Pod Autoscaler.                                                                      |
| Service Discovery<br>and Load Balancing | Basic load balancing and service discovery with Docker Swarm.                                                     | Advanced service discovery with DNS and built-in load balancing.                                                           |
| Storage Orchestration                   | Volume management with Docker volumes.                                                                            | Manages storage using persistent volumes, persistent volume claims, and dynamic provisioning.                              |
| Networking                              | Networking is simpler and managed using bridge, overlay, or host networks.                                        | Advanced networking through network plugins (CNI), supports complex networking models.                                     |
| Configuration Management                | Environment variables and Docker secrets.                                                                         | ConfigMaps and Secrets for managing configuration.                                                                         |
| Self-Healing                            | Limited to container restarts.                                                                                    | Automatically replaces and reschedules containers that fail, are terminated, or don’t respond to health checks.            |
| Community and Ecosystem                 | Strong community and extensive ecosystem of tools and services.                                                   | Larger community and broader ecosystem, supported by major cloud providers and many third-party tools.                     |

### Using Kubernetes for the Same Goals

To achieve the same goals presented in the assignment using Kubernetes, we need to:

1. Set up a Kubernetes Cluster:
    - Use a managed Kubernetes service (e.g. Google Kubernetes Engine, Amazon EKS, Azure AKS) or set up a local cluster
      using Minikube or Kind.

2. Create Kubernetes Manifests:
    - Define Kubernetes manifests for the Spring Boot application and the H2 database. These include Deployment,
      Service, ConfigMap, and PersistentVolumeClaim resources.

3. Deploy the Application and Database:
    - Use kubectl to apply the manifests and manage the lifecycle of the application.

### Conclusion

Kubernetes offers advanced orchestration features that complement Docker’s containerization capabilities. By using
Kubernetes, you can efficiently manage and scale your Spring Boot application and H2 database, ensuring high
availability and robust performance in a production environment.

[Back to Top](#table-of-contents)

# END OF README

```