# Distributed Datastore

Distributed datastore is a process to store key value pair and provide replication.

## Installation

Use the link to download [eclipse](https://www.eclipse.org/downloads/).

## Usage

1. Click on File -> Import -> Existing maven project 
2. In popup select distributed-datastore project and say ok.
3. Similarly import for naming-server project.
4. Start naming-server project 
	- Right click on com.eureka.namingserver.NamingServerApplication.java and click on "Run as Java Application". This application will run on port 8761.
5. Start distributed-datastore project which will run on port number 4455.
	- Right click on com.distributed.datastore.DistributedDatastoreApplication.java file and click on Run as Java Application.
6. Creat another instance of distributed-datastore project as per below instruction -
	- Go to Run -> Run Configuration -> duplicate DistributedDatastoreApplication and rename it with DistributedDatastoreApplication_4466 and against VM arguments you should put variable => -Dserver.port=4466
	- Run this instance
	
7. Execute a curl command to make POST request as below 
 	-  curl -H "Content-type: application/json" -XPOST http://localhost:4455/set/key -d "value"
8. Execute curl command to make GET request on another instance 
	- curl -H “Accept: application/json” http://localhost:4466/get/key
9. Make sure above GET request returns value which were set in step 7. 	
 	
 

