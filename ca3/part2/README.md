# Technical Report: CA3, Part 2 - Virtualization with Vagrant

##### Made by: [André Ferreira - 1101448]

## Table of Contents

- [Tutorial](#tutorial)
    - [Step 1: Study the Initial Solution](#step-1-study-the-initial-solution)
    - [Step 2: Update the Repository](#step-2-update-the-repository)
    - [Step 3: Start the VMs](#step-3-start-the-vms)
- [Conclusion](#conclusion)
- [Alternative solution for VirtualBox](#alternative-solution-for-virtualbox)

## Tutorial

The goal of Part 2 of this assignment is to use Vagrant to set up a virtual environment
to execute the **tutorial spring boot** application, **gradle basic version** from the previous assignment.

### Step 1: Study the Initial Solution

1. Check the initial solution provided at https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo/ to understand
   how the `Vagrantfile` is configured with two VMs.

2. The `Vagrantfile` uses provisioning scripts to automate the setup of these VMs. The **web** VM runs Tomcat and the
   Spring Boot application, while the **db** VM executes the H2 server database.

### Step 2: Update the Repository

1. Copy the `Vagrantfile` from the initial solution into the assignment repository.
   The provided `Vagrantfile` is setting up two VMs: a database VM (`db`) and a web server VM (`web`).

    - The `Vagrant.configure("2")` block is where the configuration for the VMs is defined.
    - The `config.vm.box = "ubuntu/bionic64"` line sets the base image for the VMs to be Ubuntu 18.04 (Bionic Beaver).

       ```ruby
       Vagrant.configure("2") do |config|
         config.vm.box = "ubuntu/bionic64"
       ```

    - Common provisioning for both VMs is done using a shell script that updates the package lists, installs several
      packages including `openjdk-17-jdk-headless`.

       ```ruby
       config.vm.provision "shell", inline: <<-SHELL
         sudo apt-get update -y
         sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
             openjdk-17-jdk-headless
       SHELL
       ```

    - The `db` VM is configured with a private network IP of `192.168.56.11` and two forwarded ports (`8082`
      and `9092`). It
      also downloads the H2 database jar file and sets up a provision script to always run the H2 server process on
      startup.

       ```ruby
       config.vm.define "db" do |db|
         db.vm.network "private_network", ip: "192.168.56.11"
         db.vm.network "forwarded_port", guest: 8082, host: 8082
         db.vm.network "forwarded_port", guest: 9092, host: 9092
         db.vm.provision "shell", inline: <<-SHELL
           wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar
         SHELL
         db.vm.provision "shell", :run => 'always', inline: <<-SHELL
           java -cp ./h2*.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists > ~/out.txt &
         SHELL
       end
       ```

    - The `web` VM is configured with a private network IP of `192.168.56.10` and a forwarded port (`8080`). It has more
      RAM
      allocated to it and installs several packages including `git`, `nodejs`, `npm`, and `tomcat9`. It also clones a
      GitHub
      repository, builds a project using Gradle, and deploys the resulting war file to Tomcat.

       ```ruby
       config.vm.define "web" do |web|
         web.vm.network "private_network", ip: "192.168.56.10"
         web.vm.network "forwarded_port", guest: 8080, host: 8080
         web.vm.provision "shell", inline: <<-SHELL, privileged: false
           sudo apt-get install git -y
           sudo apt-get install nodejs -y
           sudo apt-get install npm -y
           sudo ln -s /usr/bin/nodejs /usr/bin/node
           sudo apt install -y tomcat9 tomcat9-admin
           git clone https://github.com/aferreira101/devops-23-24-JPE-PSM-1101448.git
           cd devops-23-24-JPE-PSM-1101448/ca2/part2/react-and-spring-data-rest-basic
           chmod u+x gradlew
           ./gradlew clean build
           ./gradlew bootRun
           sudo cp ./build/libs/basic-0.0.1-SNAPSHOT.war /var/lib/tomcat9/webapps
         SHELL
       end
       ```

2. Update the `app.js` file in the `react-and-spring-data-rest-basic` project to use the correct API endpoint for the
   Spring Boot application running on the `web` VM, shown below:

   ```javascript
      componentDidMount()
   { // <2>
   client({method: 'GET', path: '/basic-0.0.1-SNAPSHOT/api/employees'}).done(response => {
   this.setState({employees: response.entity._embedded.employees});
   });
   }
   ```

3. Update the `index.html` file in the `react-and-spring-data-rest-basic` project to include a link to the `main.css`
   file, shown below:

   ```html
   <head lang="en">
       <meta charset="UTF-8"/>
       <title>ReactJS + Spring Data REST</title>
       <link rel="stylesheet" href="main.css" />
   </head>
   ```

4. Update the `application.properties` file in the `basic` project to set up the database connection to the H2 server
   running on the `db` VM, shown below:

   ```properties
   server.servlet.context-path=/basic-0.0.1-SNAPSHOT
   spring.data.rest.base-path=/api
   #spring.datasource.url=jdbc:h2:mem:jpadb
   # In the following settings the h2 file is created in /home/vagrant folder
   spring.datasource.url=jdbc:h2:tcp://192.168.56.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   # So that spring will no drop de database on every execution.
   spring.jpa.hibernate.ddl-auto=update
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console
   spring.h2.console.settings.web-allow-others=true
   ```

### Step 3: Start the VMs

1. First, ensure that your `repository` is set to public and that you have Vagrant installed on your machine.
   If not, download and install Vagrant from https://www.vagrantup.com/downloads.

2. Open a terminal window and navigate to the directory where the `Vagrantfile` is located.

3. Run the command `vagrant up` to start the VMs. This command will create and configure the VMs according to the
   settings in the `Vagrantfile`.

4. Once the VMs are up and running, you can access the web application by opening a web browser and navigating to one of
   the following URLs:
    - `http://localhost:8080/basic-0.0.1-SNAPSHOT/`
    - `http://192.168.56.10:8080/basic-0.0.1-SNAPSHOT/`

5. You can access the H2 database console using the following connection
   string, ` jdbc:h2:tcp://192.168.56.11:9092/./jpadb`and then open a web browser and navigate to one of the
   following URLs:
    - `http://localhost:8080/basic-0.0.1-SNAPSHOT/h2-console`
    - `http://192.168.56.10:8080/basic-0.0.1-SNAPSHOT/h2-console`

## Conclusion

Vagrant offers several benefits for setting up development environments:

1. **Consistency**:

   Vagrant allows developers to create and manage a consistent environment that is reproducible across
   multiple machines. This eliminates the "it works on my machine" problem, ensuring that the application behaves the
   same way in all environments.

2. **Isolation**:

   With Vagrant, each project can have its own isolated environment with its specific dependencies. This
   prevents conflicts between different projects that may require different versions of the same dependency.

3. **Automation**:

   Vagrant automates the process of setting up development environments, which can significantly reduce
   setup time. This is particularly useful when onboarding new team members or when you need to recreate your
   environment.

4. **Portability**:

   Vagrant environments are portable and can be shared among team members or moved between machines.
   This is facilitated by the use of Vagrant boxes, which are packaged Vagrant environments.

5. **Integration with existing tooling**:

   Vagrant works well with existing configuration management tools like Chef,
   Puppet, Ansible, etc., allowing developers to manage the setup of their machines using the same scripts they use in
   production.

6. **Multi-platform**:

   Vagrant works on many platforms like Windows, macOS, and various distributions of Linux. This
   means that no matter what operating system you or your team are using, everyone can use the same tool to manage and
   interact with their development environments.

7. **Resource Efficiency**:

   Vagrant environments are lightweight and use resources more efficiently than running
   full-fledged virtual machines, as they share resources with the host machine.

## Alternative solution for VirtualBox

As an alternative to VirtualBox, which is commonly used with Vagrant for creating and managing virtual environments,
we'll explore VMware Workstation Player. VMware Workstation Player is a widely used hypervisor that offers robust
features for virtualization, including support for multiple operating systems and hardware configurations. It's
particularly popular among developers and IT professionals for its ease of use and comprehensive feature set.

### Comparison of VMware Workstation Player vs. VirtualBox:

| Feature                        | VirtualBox                                                                              | VMware Workstation Player                                                                                                                                        |
|--------------------------------|-----------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Cost                           | Open-source and free for personal use.                                                  | Proprietary software; requires purchase for commercial use beyond a trial period.                                                                                |
| Guest Operating System Support | Supports a wide range of guest operating systems.                                       | Supports a broad range of guest operating systems and hardware configurations.                                                                                   |
| Basic Virtualization Features  | Offers basic virtualization features such as shared folders, networking, and snapshots. | Provides advanced virtualization features, including better performance optimization, enhanced security options, and more sophisticated networking capabilities. |
| Interface                      | Has a simpler interface compared to VMware Workstation Player.                          | Offers a more intuitive and feature-rich interface than VirtualBox, with tools for cloning VMs, creating custom virtual hardware profiles, and more.             |

### Integration with Vagrant:

While Vagrant traditionally works well with VirtualBox due to its simplicity and direct integration through plugins,
it's also possible to use VMware Workstation Player with Vagrant, albeit with some additional configuration steps.

1. Download and Install VMware Workstation Player:
    - Go to the official VMware website and download the VMware Workstation Player for Windows. Follow the installation
      instructions provided.

2. Configure Vagrant to Use VMware Workstation Player:
    - Since VMware Workstation Player doesn't come with built-in Vagrant support like VirtualBox, you'll need to specify
      the provider in your Vagrantfile. Here's an example configuration:

   ```ruby
   Vagrant.configure("2") do |config|
   config.vm.box = "base"
   
   # Specify the provider
   config.vm.provider :vmware_workstation do |vm|
   vm.guest_os_type = 'windows9Guest' # Adjust based on your guest OS type
   end
   
   # Other configurations...
   end
   ```

   In this configuration, replace `base` with the name of your Vagrant box. Also, adjust the `guest_os_type` according
   to the operating system you plan to run inside the VM. For example, `windows10_64Guest` for Windows 10, or
   `ubuntu64Guest` for Ubuntu 18.04 LTS.

3. Start Your VM:

   With the `Vagrantfile` configured, you can now start your VM using the `vagrant up`command. Vagrant will use VMware
   Workstation Player as the provider to create and manage the VM.

Switching from VirtualBox to VMware Workstation Player on Windows provides access to a more powerful and flexible
virtualization platform, enhancing productivity and performance for development tasks. While there's a learning curve
and potentially higher costs involved, the benefits of VMware Workstation Player's advanced features and compatibility
make it a worthwhile consideration for professional and advanced users.

[Back to Top](#table-of-contents)

# END OF README

```
