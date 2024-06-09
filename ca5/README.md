# Technical Report: CA5 - CI/CD Pipelines with Jenkins

###### Made by: [André Ferreira - 1101448]

## Table of Contents

- [Tutorial](#tutorial)
    - [Part 1: Setting up Jenkins Pipeline for Gradle Basic Demo Application](#part-1-setting-up-jenkins-pipeline-for-gradle-basic-demo-application)
        - [Prerequisites](#prerequisites)
        - [Pipeline Configuration](#pipeline-configuration)
            - [Jenkinsfile](#jenkinsfile)
        - [Create a New Pipeline Job in Jenkins](#create-a-new-pipeline-job-in-jenkins)
        - [Run the Pipeline](#run-the-pipeline)
        - [Conclusion](#conclusion)
    - [Part 2: Setting up Jenkins Pipeline for React and Spring Data REST Basic Application](#part-2-setting-up-jenkins-pipeline-for-react-and-spring-data-rest-basic-application)
        - [Prerequisites](#prerequisites-1)
        - [Pipeline Configuration](#pipeline-configuration-1)
            - [Jenkinsfile](#jenkinsfile-1)
        - [Configure Docker Credentials in Jenkins](#configure-docker-credentials-in-jenkins)
        - [Create a New Pipeline in Jenkins](#create-a-new-pipeline-in-jenkins)
        - [Run the Pipeline](#run-the-pipeline-1)
        - [Conclusion](#conclusion-1)

## Tutorial

The tutorial is divided into two parts, each focusing on a specific application. The first part covers setting up a
Jenkins pipeline for the Gradle Basic Demo application from CA2/part1, while the second part covers setting up a Jenkins
pipeline for the React and Spring Data REST application from CA2/part2. The pipelines are defined using Jenkinsfile
declarative syntax and include stages for building, testing, generating Javadoc, and creating a Docker image. The
pipelines are configured to run on a Jenkins server and automate the process of building, testing, and deploying the
applications. The tutorial provides step-by-step instructions on how to create the pipelines in Jenkins and run them to
build and deploy the applications.
The pipelines are designed to provide feedback on the build status and generate reports for test results and Javadoc.
The Docker image created in the second part of the tutorial is pushed to Docker Hub for deployment. The tutorial
demonstrates how to configure Jenkins, set up the pipelines, and run them to automate the CI/CD process for the two
applications.

### Part 1: Setting up Jenkins Pipeline for Gradle Basic Demo Application

This part contains a Jenkins pipeline configuration for building and testing the Gradle Basic Demo application from
CA2/part1.

#### Prerequisites

Before you begin, ensure you have the following installed on your Jenkins server:

- Jenkins
- Git

#### Pipeline Configuration

The Jenkins pipeline is defined in the `Jenkinsfile` located in the project directory. Below is an overview of the
stages involved in the pipeline:

1. **Checkout**: Clones the project repository from GitHub.
2. **Assemble**: Builds the project using Gradle.
3. **Test**: Runs the tests and records the results.
4. **Archive**: Archives the built JAR file.

##### Jenkinsfile

```groovy
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out from repository'
                git branch: 'main', url: 'https://github.com/aferreira101/devops-23-24-JPE-PSM-1101448.git'
            }
        }
        stage('Assemble') {
            steps {
                dir('ca2/part1/gradle_basic_demo') {
                    echo 'Assembling...'
                    sh 'chmod +x gradlew'
                    sh './gradlew clean assemble'
                }
            }
        }
        stage('Test') {
            steps {
                dir('ca2/part1/gradle_basic_demo') {
                    echo 'Running Tests...'
                    sh './gradlew test'
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
        stage('Archive') {
            steps {
                dir('ca2/part1/gradle_basic_demo') {
                    echo 'Archiving artifacts...'
                    archiveArtifacts artifacts: 'build/libs/*.jar', allowEmptyArchive: true
                }
            }
        }
    }
}
```

#### Create a New Pipeline Job in Jenkins

1. Go to Jenkins and click on `New Item`.
2. Enter a name for the pipeline and select `Pipeline`.
3. Under `Pipeline`, select `Pipeline script from SCM`.
4. Choose `Git` as the SCM and enter the repository URL.
5. Enter `main` as the branch.
6. In the `Script Path`, enter `ca5/gradle_basic_demo/Jenkinsfile`.
7. Click on `Save`.

#### Run the Pipeline

1. Click on `Build Now` to run the pipeline.
2. Monitor the progress of the pipeline in the Jenkins console output.

#### Conclusion

In this part, a Jenkins pipeline was created to build and test the Gradle Basic Demo application from CA2/part1. The
pipeline automates the process of building the application and running tests, providing feedback on the build status.

### Part 2: Setting up Jenkins Pipeline for React and Spring Data REST Basic Application

This part contains a Jenkins pipeline configuration for building, testing, generating Javadoc, and creating a
Docker image for the React and Spring Data REST application from CA2/part2.

#### Prerequisites

Before you begin, ensure you have the following installed on your Jenkins server:

- Jenkins
- Docker
- Git

#### Pipeline Configuration

The Jenkins pipeline is defined in the `Jenkinsfile` located in the project directory. Below is an overview of the
stages
involved in the pipeline:

1. **Checkout**: Clones the repository from GitHub.
2. **Create Dockerfile**: Creates a `Dockerfile` if it doesn't exist.
3. **Assemble**: Builds the project using Gradle.
4. **Test**: Runs unit tests and records the results.
5. **Javadoc**: Generates Javadoc and publishes it as an HTML report.
6. **Archive**: Archives the built JAR file.
7. **Build Docker Image**: Builds a Docker image and pushes it to Docker Hub.

##### Jenkinsfile

```groovy
pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'docker-credentials'
        DOCKER_IMAGE = "1101448/jenkins-image"
        DOCKER_REGISTRY = "https://index.docker.io/v1/"
        REPO_URL = 'https://github.com/aferreira101/devops-23-24-JPE-PSM-1101448.git'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out from repository...'
                git branch: 'main', url: env.REPO_URL
            }
        }
        stage('Create Dockerfile') {
            steps {
                dir('ca2/part2/react-and-spring-data-rest-basic') {
                    echo 'Creating Dockerfile...'
                    script {
                        writeFile file: 'Dockerfile', text: '''
                        # Use the official OpenJDK image as a parent image
                        FROM openjdk:17-jdk-alpine

                        # Set the working directory in the container
                        WORKDIR /app

                        # Copy the JAR file from the host to the container
                        COPY build/libs/*.jar app.jar

                        # Expose the port that the application will run on
                        EXPOSE 8080

                        # Run the JAR file
                        ENTRYPOINT ["java", "-jar", "app.jar"]
                        '''
                    }
                }
            }
        }
        stage('Assemble') {
            steps {
                dir('ca2/part2/react-and-spring-data-rest-basic') {
                    echo 'Assembling...'
                    sh 'chmod +x gradlew'
                    sh './gradlew clean assemble'
                }
            }
        }
        stage('Test') {
            steps {
                dir('ca2/part2/react-and-spring-data-rest-basic') {
                    echo 'Testing...'
                    sh './gradlew test'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        stage('Javadoc') {
            steps {
                dir('ca2/part2/react-and-spring-data-rest-basic') {
                    echo 'Generating Javadoc...'
                    sh './gradlew javadoc'
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/docs/javadoc', reportFiles: 'index.html', reportName: 'Javadoc'])
                }
            }
        }
        stage('Archive') {
            steps {
                dir('ca2/part2/react-and-spring-data-rest-basic') {
                    echo 'Archiving...'
                    archiveArtifacts artifacts: 'build/libs/*.jar', allowEmptyArchive: true
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                dir('ca2/part2/react-and-spring-data-rest-basic') {
                    script {
                        echo 'Building Docker Image...'
                        sh 'docker info'
                        def app = docker.build("${env.DOCKER_IMAGE}:${env.BUILD_ID}", '.')
                        docker.withRegistry(env.DOCKER_REGISTRY, env.DOCKER_CREDENTIALS_ID) {
                            app.push()
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
```

#### Configure Docker Credentials in Jenkins

1. Go to Manage Jenkins > Manage Credentials > System > Global credentials (unrestricted) > Add Credentials.
2. Select `Username with password` as the kind.
3. Enter your Docker Hub username and password.
4. Set the ID to `docker-credentials`.

#### Create a New Pipeline in Jenkins

1. Go to Jenkins and click on `New Item`.
2. Enter a name for the pipeline and select `Pipeline`.
3. Under `Pipeline`, select `Pipeline script from SCM`.
4. Choose `Git` as the SCM and enter the repository URL.
5. Enter `main` as the branch.
6. In the `Script Path`, enter `ca5/react-and-spring-data-rest-basic/Jenkinsfile`.
7. Click on `Save`.

#### Run the Pipeline

1. Click on `Build Now` to run the pipeline.
2. Monitor the progress of the pipeline in the Jenkins console output.

#### Conclusion

In this part, a Jenkins pipeline was created to build, test, generate Javadoc, and create a Docker image for the React
and Spring Data REST application from CA2/part2. The pipeline automates the process of building and deploying the
application to Docker Hub.

[Back to Top](#table-of-contents)

# END OF README

```