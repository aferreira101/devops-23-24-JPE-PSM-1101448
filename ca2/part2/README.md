# Technical Report: Build Tools with Gradle

###### Made by: [André Ferreira - 1101448]

## Table of Contents

- [Tutorial](#tutorial)
    - [Step 1: Create a New Branch](#step-1-create-a-new-branch)
    - [Step 2: Generate a New Gradle Spring Boot Project & Extract the Zip](#step-2-generate-a-new-gradle-spring-boot-project--extract-the-zip)
    - [Step 3: Delete the src folder and copy the Basic Tutorial Code](#step-3-delete-the-src-folder-and-copy-the-basic-tutorial-code)
    - [Step 4: Add frontend plugin to the build.gradle file](#step-4-add-frontend-plugin-to-the-buildgradle-file)
    - [Step 5: Build the Project & Run the Application](#step-5-build-the-project--run-the-application)
    - [Step 6: Add a Task to Copy the generated JAR](#step-6-add-a-task-to-copy-the-generated-jar)
    - [Step 7: Add a Task to Delete Webpack Generated Files](#step-7-add-a-task-to-delete-webpack-generated-files)
- [Alternative](#alternative)

## Tutorial

### Step 1:  Create a New Branch

Open your terminal and navigate to the directory where you have the repository cloned. Then, run the following command
to create a new branch and switch to it:

- `git checkout -b tut-basic-gradle`

### Step 2: Generate a New Gradle Spring Boot Project & Extract the Zip

1. Go to the [Spring Initializr](https://start.spring.io/) website and generate a new project with the following
   settings:

    - Project: Gradle Project
    - Language: Java
    - Spring Boot: 3.2.4
    - Project Metadata:
        - Group: com.greglturnquist
        - Artifact: react-and-spring-data-rest-basic
        - Name: react-and-spring-data-rest-basic
        - Description: Demo project for Spring Boot
        - Package Name: com.greglturnquist.payroll
        - Packaging: Jar
        - Java: 17
    - Dependencies:
        - Rest Repositories (WEB)
        - Thymeleaf (TEMPLATE ENGINES)
        - Spring Data JPA (SQL)
        - H2 Database (SQL)

2. Click on the "Generate" button to download the project as a zip file. Extract the content of the zip file into
   the `ca2/part2` directory of the repository.

### Step 3:  Delete the `src` folder and copy the Basic Tutorial Code

Go to the `src` folder of the extracted project and delete it. Then, copy the content of the `src` folder,
the `webpack.config.js` and the `package.json` files from the `ca1` to the `ca2/part2` directory. Since the
`src/main/resources/static/built` folder should be generated from the javascript by the webpack tool, delete it.

### Step 4:  Add frontend plugin to the `build.gradle` file

To add the frontend plugin to the `build.gradle` file, follow these steps:

1. Open the `build.gradle` file and add one of the following code to the `plugins` section(use the one that corresponds
   to the version of Java you are using):

   ```groovy
   id "org.siouan.frontend-jdk8" version "6.0.0"
   id "org.siouan.frontend-jdk11" version "8.0.0"
   id "org.siouan.frontend-jdk17" version "8.0.0"
   ```
   This will allow Gradle to handle the frontend build tasks, such as running Webpack, directly from your Gradle build
   script.

2. In the `frontend` section, add the following code:

   ```groovy
   frontend {
    nodeVersion = "16.20.2"
    assembleScript = "run build"
    cleanScript = "run clean"
    checkScript = "run check"
   }
   ```

   This configuration tells the plugin to use Node.js version 16.20.2 and specifies the npm scripts to run for building,
   cleaning, and checking your frontend.

3. Next, open the `package.json` file and add the following scripts:

   ```json
   "scripts": {
      "webpack": "webpack",
      "build": "npm run webpack",
      "check": "echo Checking frontend",
      "clean": "echo Cleaning frontend",
      "lint": "echo Linting frontend",
      "test": "echo Testing frontend"
   }
   ```

   This configuration defines several npm scripts:
    - `webpack`: Runs webpack to bundle your frontend code.
    - `build`: Executes the webpack script, which is defined as npm run webpack.
    - `check`: A placeholder script that echoes "Checking frontend".
    - `clean`: A placeholder script that echoes "Cleaning frontend".
    - `lint`: A placeholder script that echoes "Linting frontend".
    - `test`: A placeholder script that echoes "Testing frontend".

### Step 5:  Build the Project & Run the Application

Open your terminal and navigate to the `ca2/part2` directory of the repository. Then, run the following command to build
the project:

- `./gradlew build`

After the build is successful, run the following command to start the application:

- `./gradlew bootRun`

Open your browser and go to `http://localhost:8080` to see the application running.

### Step 6: Add a Task to Copy the generated JAR

To add a task to copy the generated JAR file to the `ca2/part2` directory, open your `build.gradle` file and add the
following code:

   ```groovy
   task copyJarToDist(type: Copy) {
    from tasks.jar
    into 'dist'
}
   ```

This task will copy the generated JAR file to the `dist` directory. To run this task, execute the following command:

- `./gradlew copyJarToDist`

After running the task, you should see the JAR file in the `dist` directory.

### Step 7: Add a Task to Delete Webpack Generated Files

To add a task to delete the Webpack generated files, open your `build.gradle` file and add the following code:

   ```groovy
   task deleteWebpackFiles(type: Delete) {
    delete 'src/main/resources/static/built'
}
   ```

This task will delete the `src/main/resources/static/built` directory which contains the Webpack generated files.
To ensure that this task is executed after the `clean` task, add the following line to the `clean` task:

   ```groovy
   clean {
    dependsOn deleteWebpackFiles
}
   ```

This modification ensures that every time you run ./gradlew clean, Gradle first runs the deleteWebpackFiles task to
delete the webpack-generated files before proceeding with the cleanup. To test this task, run the following command:

- `./gradlew deleteWebpackFiles`

After running the task, you should see that the `src/main/resources/static/built` directory has been deleted.

## Alternative

The alternative technological solution for build automation can be Maven. Maven is a build automation tool used
primarily for Java projects. It is similar to Gradle but uses an XML-based configuration file (pom.xml) to define the
project's structure, dependencies, and build process. Maven provides a set of plugins and goals to automate the build
process, including compiling source code, running tests, packaging the application, and deploying it to a server.

|    Feature/Aspects    | Maven                                                                                                     | Gradle                                                                                                                         |
|:---------------------:|-----------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| Dependency Management | Uses pom.xml for dependency management.                                                                   | Uses build.gradle for dependency management, offering more flexibility and efficiency in multi-project builds.                 |
|     Plugin System     | Has a rich ecosystem of plugins available through the Maven Central Repository.                           | Offers a robust plugin system with the ability to write custom plugins in Groovy or Kotlin, providing more flexibility.        |
|    Task Management    | Task management is more static and relies on plugins for extending the build process.                     | Task management is dynamic, allowing for tasks to be defined and extended at runtime.                                          |
|      Performance      | Generally slower, especially in multi-module projects.                                                    | Faster, especially for incremental builds.                                                                                     |
|     Extensibility     | Extensibility is limited to plugins, with creating custom tasks requiring more effort.                    | Highly extensible, allowing for custom tasks to be defined and extended at runtime.                                            |
| New Plugins or Tasks  | Adding new plugins or tasks requires modifying the pom.xml file and following the plugin's documentation. | Adding new plugins or tasks is straightforward, with plugins being included in the build.gradle file.                          |
|  Frontend Management  | Requires the frontend-maven-plugin for managing frontend builds.                                          | Can use the org.siouan.frontend Gradle plugin for frontend build tasks, including webpack execution.                           |
|     Custom Tasks      | Creating custom tasks requires writing Maven plugins or using existing plugins.                           | Creating custom tasks is straightforward, with tasks being defined in the build.gradle file.                                   |
|       Use Case        | Ideal for Java projects with a focus on dependency management and a rich ecosystem of plugins.            | Offers more flexibility and efficiency for multi-language projects, with a focus on dynamic task management and extensibility. |

To use Maven as an alternative to Gradle, you would need to create a new Maven project and configure the `pom.xml` file
with the necessary dependencies, plugins, and build settings. For the frontend build tasks, you would need to use the
frontend-maven-plugin to execute Webpack and bundle the frontend code. And for custom tasks, you would need to write
Maven plugins or use existing plugins to extend the build process. While Maven is a powerful build automation tool,
Gradle offers more flexibility, extensibility, and efficiency, making it a preferred choice for many developers.


[Back to Top](#table-of-contents)

# END OF README

```