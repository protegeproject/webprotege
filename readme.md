AxiomatIQ
==========

Building
--------

To build AxiomatIQ from source

1) Clone the GitHub repository
   ```
   git clone https://github.com/Jiayi-Zeng/webprotege.git
   ```
2) Open a terminal in the directory where you clone the repository to
3) Use maven to package AxiomatIQ
   ```
   mvn clean package
   ```
4) The AxiomatIQ .war file will be built into the webprotege-server directory

Running from Maven
------------------

To run AxiomatIQ in SuperDev Mode using maven

1) Start the GWT code server in one terminal window
    ```
    mvn gwt:codeserver
    ```
2) In a different terminal window start the tomcat server
    ```
    mvn -Denv=dev tomcat7:run
    ```
3) Browse to AxiomatIQ in a Web browser by navigating to [http://localhost:8080](http://localhost:8080)

Running from Docker
-------------------

To run AxiomatIQ using Docker containers:

1. Enter this following command in the Terminal to start the docker container in the background

   ```bash
   docker-compose up -d
   ```

2. Create the admin user (follow the questions prompted to provider username, email and password)

   ```bash
   docker exec -it webprotege java -jar /webprotege-cli.jar create-admin-account
   ```

3. Browse to AxiomatIQ Settings page in a Web browser by navigating to [http://localhost:5000/#application/settings](http://localhost:5000/#application/settings)
   1. Define the `System notification email address` and `application host URL`
   2. Enable `User creation`, `Project creation` and `Project import`

To stop AxiomatIQ and MongoDB:

   ```bash
   docker-compose down
   ```

Sharing the volumes used by the AxiomatIQ app and MongoDB allow to keep persistent data, even when the containers stop. Default shared data storage:

* AxiomatIQ will store its data in the source code folder at `./.protegedata/protege` where you run `docker-compose`
* MongoDB will store its data in the source code folder at `./.protegedata/mongodb` where you run `docker-compose`

> Path to the shared volumes can be changed in the `docker-compose.yml` file.
