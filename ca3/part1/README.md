# Technical Report: CA3, Part 1 - Virtualization with Vagrant

##### Made by: [André Ferreira - 1101448]

## Table of Contents

- [Tutorial](#tutorial)
    - [Step 1: Setting Up VirtualBox/UTM VM with Ubuntu](#step-1-setting-up-virtualboxutm-vm-with-ubuntu)
    - [Step 2: Installing Dependencies](#step-2-installing-dependencies)
    - [Step 3: Cloning the Repository](#step-3-cloning-the-repository)
    - [Step 4: Building and Executing the Projects](#step-4-building-and-executing-the-projects)
- [Conclusion](#conclusion)

## Tutorial

The goal of Part 1 of this assignment is to practice with VirtualBox using the same projects from the previous
assignments but now inside a VirtualBox VM with Ubuntu

### Step 1: Setting Up VirtualBox/UTM VM with Ubuntu

1. Download and Install VirtualBox:

    - Go to the VirtualBox download page and download the version suitable for your operating
      system. Install it following the instructions provided.

2. Download Ubuntu ISO:

    - Go to the Ubuntu download page and download the latest LTS version of Ubuntu. This version is
      recommended for stability and long-term support.

3. Create a New Virtual Machine:

    - Open VirtualBox and click on "New" to create a new virtual machine.
    - Name your VM, select "Linux" as the type, and "Ubuntu (64-bit)" as the version.
    - Allocate memory (RAM) to your VM. A minimum of 2GB is recommended for a smooth experience.
    - Create a virtual hard disk now or later. Select VDI (VirtualBox Disk Image) as the hard disk file type. Allocate
      disk space according to your needs (at least 20GB is recommended).

4. Install Ubuntu:

    - Start the VM and follow the on-screen instructions to install Ubuntu. You'll need to create a user account and
      password.

### Step 2: Installing Dependencies

1. Update and Upgrade Ubuntu:

    - Open the terminal in your VM.
    - Run `sudo apt update` and `sudo apt upgrade` to ensure your system is up to date.

2. Install Git:

    - Run `sudo apt install git` to install Git.

3. Install JDK and JRE:

    - Run `sudo apt install openjdk-17-jdk openjdk-17-jre` to install the OpenJDK 17 JDK and JRE.

4. Install Maven:

    - Run `sudo apt install maven` to install Maven.

5. Install Gradle:

    - Run `wget https://services.gradle.org/distributions/gradle-8.6-bin.zip` to download the Gradle distribution.
    - Run `sudo mkdir /opt/gradle` to create a directory for Gradle.
    - Run `sudo unzip -d /opt/gradle gradle-8.6-bin.zip` to extract the Gradle distribution.
    - Run `export PATH=$PATH:/opt/gradle/gradle-8.6/bin` to add Gradle to your PATH.
    - Run `gradle -v` to verify the installation.

### Step 3: Cloning the Repository

1. Generate SSH Key:

    - In the VM terminal, run `ssh-keygen -t rsa -b 4096 -C "your_email@example.com"` to generate an SSH key.
    - Press Enter to save the key in the default location. `~/.ssh/id_rsa`.
    - Add the SSH key to the SSH agent by running `eval "$(ssh-agent -s)"` and `ssh-add ~/.ssh/id_rsa`.
    - Copy the SSH key to your clipboard by running `cat ~/.ssh/id_rsa.pub` and copying the output.

2. Add SSH Key to GitHub:

    - Go to your GitHub account settings and click on "SSH and GPG keys".
    - Click on "New SSH key" and paste the SSH key you copied earlier.
    - Click on "Add SSH key" to add the key to your GitHub account.

3. Clone Your Repositories:

    - Use the git clone command followed by the URL of your repository to clone it into your VM. For example:
     ```bash
     git clone git@github.com:yourusername/your_repository.git
     ```

### Step 4: Building and Executing the Projects

Navigate to Project Directory:

- Use the cd command to navigate to the project directory. In this case, go inside the basic folder.

1. Build and Run the Project from the previous class assignment **Spring boot tutorial**:

    - Run `./mvnw spring-boot:run` to run the project in the VM.
    - Open a web browser in your desktop and go to `http://192.168.56.5:8080` to see the project running.

2. Build and Run the Project from the previous class assignment **Gradle basic demo part 1**:

    - Run `./gradlew build` to build the project in the VM.
    - Run `.gradlew runClient --args="192.168.56.5 59001"` to run the client in your 'real' machine, this is a crucial
      step since the VM is not able to run the client due to the lack of GUI.

3. Build and Run the Project from the previous class assignment **Gradle basic demo part 2**:

    - Run `./gradlew build` to build the project in the VM.
    - Run `.gradlew bootRun` to run the project in the VM.
    - Open a web browser in your desktop and go to `http://192.168.56.5:8080` to see the project running.

### Conclusion

This tutorial covered the steps to set up a VirtualBox VM with Ubuntu, install the necessary dependencies, clone the
repository, and build and run the projects from the previous class assignments all this inside the VM. This is a good
practice to get familiar with virtualization and to ensure that the projects can run in different environments.

[Back to Top](#table-of-contents)

# END OF README

```