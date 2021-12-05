## Ontology attestation and verification module for WebProtégé

A module for WebProtégé, implementing an approach for ontology attestation.

## Datasets
* [ISWC21](dataset/ISWC21)
* [KCAP21](dataset/KCAP21)

## Requirements
* solc 7.1.0
* web3j-cli
* node and npm
* ganache-cli
* docker-compose
* Java 8 + Maven

## Building the module
The module can be compiled and installed locally with `mvn clean install`. The smart contracts need to be compiled and deployed separately.

## Compiling and deploying the smart contracts
The `scripts` folder contains utilities for compiling and deploying the contracts.
Compile solidity contracts and generate interfaces and wrappers:
```
sh ./compile-contracts.sh
```
Start a local Ganache test blockchain with the docker-compose file in the [parent project](../readme.md) (execute from the project root).
```
docker-compose up -d ganache
```
The seed is fixed, use the mnemonic indicated in the docker-compose file to access the test accounts.

Deploy contracts (execute from the `scripts` folder):
```
npm install
npm run deploy
```

## Configuration
The contract address must be made available to the application server. To do this, configure the address in `src/main/java/resources/configuration/config.properties`.
Alternatively, the address may be set n the environment with the key `ADDRESS_ATTESTATION`, the RPC provider host and port by `PROVIDER_HOST` and `PROVIDER_PORT` respectively.

## Dataset & Running the tests
The evaluation dataset and test results can be found in the folder `dataset/`.
The tests require the ontologies to be placed in `src/test/resources/ontologies`. 

Configuration of the test blockchain provider are located in `src/test/resources/configuration`. The tests use the JUnit 
framework and can thus be run manually by an appropriate runner, e.g., through Intellij.
