# Medical-Management-System-Batch
This is a Spring Batch application that converts the list of the users from a csv file into mySQL database.

# Installation 
* Java Development Kit (JDK): Ensure that you have Java installed on your system. Spring Batch requires Java to run. You can download the latest version of JDK from the official Oracle website or use a package manager if available on your operating system.

* Build Tool: The build tool for this project is Maven. To install Maven use this link: https://maven.apache.org/download.cgi

* Project Setup: Clone or download the project repository from your version control system (e.g., Git). https://github.com/RomeisaAliu/Medical-Management-System-Batch.git

* Dependency Management: If you are using Maven the project's dependencies are managed using the build tool's configuration files: pom.xml for Maven. Users need to run a command to resolve and download the dependencies. Commands to install the project dependencies: mvn clean install

* Database Setup: This Spring Batch project uses a MySQL database. The required database server MySQL is Xampp connection or with Apache server.

* Configuration: In the pom.xml you can add any dependencies and in the application.properties you can change the database username and password because it depends on the local MySQL.Ensure that you have the required dependencies for Spring Batch, such as spring-boot-starter-batch and spring-boot-starter-data-jpa, in your project's build file pom.xml .

* Running the Application: To run the Spring Batch application you can simple run from IntelliJ to start the application and try with postman the POST method in the right path.


# Spring Batch Components: Batch contains the job and step flows configuration.

* Job:A single execution unit that summarises a series of processes for batch application in Spring Batch.

* Step:A unit of processing which constitutes Job. 1 job can contain 1~N steps.
 Reusing a process, parallelization, conditional branching can be performed by dividing 1 job process in multiple steps. 
 Step is implemented by either chunk model or tasket model.
 
* JobLauncher: An interface for running a Job. JobLauncher can be directly used by the user, however, a batch process can be started simply
by starting CommandLineJobRunner from java command. CommandLineJobRunner undertakes various processes for starting JobLauncher.

* ItemReader | ItemProcessor | ItemWriter :
An interface for dividing into three processes - input / processing / output of data while implementing chunk model.
Batch application consists of processing of these 3 patterns AND in Spring Batch implementation of these interfaces is utilized primarily in chunk model.
Since ItemReader and ItemWriter responsible for data input and output are often the processes that perform conversion of database and files to Java objects and vice versa.
ItemProcessor which is responsible for processing data implements input check and business logic.
In general batch applications which perform input and output of data from file and database, conditions can be satisfied just by using standard implementation of Spring Batch.

* In Tasket model: ItemReader/ItemProcessor/ItemWriter substitutes a single Tasklet interface implementation.

* JobRepository: A system to manage condition of Job and Step. The management information is persisted on the database based on the table schema specified by Spring Batch.
