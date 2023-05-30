# Medical-Management-System-Batch
This is a Spring Batch application that converts the list of the users from a csv file into mySQL database.

#Spring Batch Components: Batch contains the job and step flows configuration.

-Job:A single execution unit that summarises a series of processes for batch application in Spring Batch.

-Step:A unit of processing which constitutes Job. 1 job can contain 1~N steps.
 Reusing a process, parallelization, conditional branching can be performed by dividing 1 job process in multiple steps. 
 Step is implemented by either chunk model or tasket model.
 
-JobLauncher: An interface for running a Job. JobLauncher can be directly used by the user, however, a batch process can be started simply
by starting CommandLineJobRunner from java command. CommandLineJobRunner undertakes various processes for starting JobLauncher.

-ItemReader | ItemProcessor | ItemWriter :
An interface for dividing into three processes - input / processing / output of data while implementing chunk model.
Batch application consists of processing of these 3 patterns AND in Spring Batch implementation of these interfaces is utilized primarily in chunk model.
Since ItemReader and ItemWriter responsible for data input and output are often the processes that perform conversion of database and files to Java objects and vice versa.
ItemProcessor which is responsible for processing data implements input check and business logic.
In general batch applications which perform input and output of data from file and database, conditions can be satisfied just by using standard implementation of Spring Batch.

In Tasket model: ItemReader/ItemProcessor/ItemWriter substitutes a single Tasklet interface implementation.

-JobRepository: A system to manage condition of Job and Step. The management information is persisted on the database based on the table schema specified by Spring Batch.
