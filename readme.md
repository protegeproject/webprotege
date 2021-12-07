Fork of WebProtégé implementing ontology attestation and verification
==========

Modification of the [WebProtégé ontology editor](https://github.com/protegeproject/webprotege) to support ontology attestation by implementing a custom portlet. See the original readme below for 
instructions for WebProtégé in general. To run a pre-built docker image see the section ["Running from docker"](#running-from-docker).

## Relevant links
- For further documentation see the [Attesattion module](webprotege-attestation) and its [readme](webprotege-attestation/readme.md)
- Attestation portlet [usage guide](docs/guide.md).(webprotege-attestation/docs/guide.md)
- [Dataset](https://github.com/curtys/webprotege-attestation/tree/master/dataset) and results used for publication

## Requirements

- Development requirements of the attestation module for compiling and deploying smart contracts:
   - [solc 7.1.0+](https://docs.soliditylang.org/en/v0.8.10/installing-solidity.html#linux-packages) 
   - [web3j-cli](http://docs.web3j.io/4.8.7/command_line_tools/) 
   - [Node and NPM](https://nodejs.org/en/)
   - [Metamask](metamask.io) (or other wallet plugin) for client blockchain interaction
- [Docker Compose](https://docs.docker.com/compose/install/)
- Java 8 and Maven

## Building for development

To build WebProtégé from source

1. Clone the github repository
2. Open a terminal in the directory where you clone the repository
3. Use maven to package WebProtégé
   ```
   mvn clean package
   ```
4. The WebProtege .war file will be built into the webprotege-server directory

## Starting in DevMode

- first, consider following the instructions of the **attestation module** to set up an Ethereum test network
- start the database with `docker-compose up -d wpmongo`
- start the GWT code server with `mvn gwt:codeserver`
- in a different terminal start a tomcat server instance with `mvn -P dev -Denv=dev tomcat7:run`. The maven profile `-P dev` will result in WebProtégé storing its data in `.devdata` instead of the default directory (`/srv/webprotege`).
- first time starting up (or after resetting the database) an admin user has to be created. 
  Executed the **webprotege-cli** JAR (compile it, if not already done). E.g., 
  `java -jar webprotege-cli/target/webprotege-cli-{version}.jar create-admin-account`
- by default, WebProtégé is available on [http://localhost:8080](http://localhost:8080).
- After the first start, some application settings need to be configured. Login to [localhost:8080/#application/settings](the settings page) with the previously created admin account.

Detailed installation instructions can be found in the [official wiki](https://github.com/protegeproject/webprotege/wiki/WebProt%C3%A9g%C3%A9-4.0.0-Installation).

## Running from Docker

A pre-built docker image is available. To run the project stack from docker:

1. Enter this following command in the Terminal to start the docker container in the background. This will start containers for all dependand services as well, i.e., MongoDB and Ganache.

   ```bash
   docker-compose up -d
   ```
1. Deploy the smart contracts (e.g., to Ganache). Follow the instructions [here](webprotege-attestation/README.md).

1. Create the admin user (follow the questions prompted to provider username, email and password)

   ```bash
   docker exec -it webprotege java -jar /webprotege-cli.jar create-admin-account
   ```

1. Browse to WebProtégé Settings page in a Web browser by navigating to [http://localhost:5000/#application/settings](http://localhost:5000/#application/settings)
    1. Define the `System notification email address` and `application host URL`
    2. Enable `User creation`, `Project creation` and `Project import`

To stop WebProtégé, MongoDB and the Ganache test instance:

   ```bash
   docker-compose down
   ```

Sharing the volumes used by the WebProtégé app and MongoDB allow to keep persistent data, even when the containers stop. Default shared data storage:

* WebProtégé will store its data in the source code folder at `./.protegedata/protege` where you run `docker-compose`
* MongoDB will store its data in the source code folder at `./.protegedata/mongodb` where you run `docker-compose`

> Path to the shared volumes can be changed in the `docker-compose.yml` file.

By default, a Ganache test blockchain will be started. WebProtégé is pre-configured to use this chain. It is possible to change the chain by setting environment variables in `docker-compose.yml` (of the servcie `webprotege-attestation`):
```yaml
environment:
      - webprotege.mongodb.host=wpmongo
      - ADDRESS_ATTESTATION=<the smart contract address>
      - PROVIDER_HOST=<host name of the RPC endpoint provider>
      - PROVIDER_PORT=<port of the RPC endpoint provider>  
```

## Changes made in this fork
Some key classes were altered to integrate an attestation 
portlet in the editor. The portlet can be added by choosing 'Attestation' from the list of available portlets, 
e.g. when adding a new tab.
In the graphic below the altered classes are marked in red and the added packages in green.

![docs/package_protege.png](docs/package_protege.png)
