## Ontology attestation and verification module for WebProtégé

A module for WebProtégé, implementing an approach for ontology attestation. See also the [usage guide](docs/guide.md).

## Datasets
* [ISWC21](dataset/ISWC21)

## Development requirements
* [solc 7.1.0+](https://docs.soliditylang.org/en/v0.8.10/installing-solidity.html#linux-packages)
* [web3j-cli](http://docs.web3j.io/4.8.7/command_line_tools/)
* [Node and NPM](https://nodejs.org/en/)
* [Ganache / Ganache CLI](https://trufflesuite.com/ganache/)
* [Metamask](metamask.io) (or other wallet plugin) for client blockchain interaction
* Java 8 + Maven

## Building the module
The module can be compiled and installed locally with `mvn clean install`. The smart contracts need to be compiled and deployed separately.

## Compiling and deploying the smart contracts
The `scripts` folder contains utilities for compiling and deploying the contracts.
1. Compile solidity contracts (requires `solc`):
    ```
    sh ./compile-contracts.sh
    ```
2. Generate Java contract interfaces and wrappers (only needed for development purposes, requires `web3j-cli` and `solc`):
    ```
    sh ./generate-contract-wrappers.sh
    ```
3. Start a local Ganache test blockchain with the docker-compose file in the [parent directory](../readme.md) (execute from the project root).
    ```
    docker-compose up -d ganache
    ```
4. The seed is fixed, use the mnemonic indicated in the docker-compose file to access the test accounts (in Metamask this is the "Secret Recovery Phrase").

5. Deploy contracts (execute from the `scripts` folder, requires `node` and `npm`):
    The connection to the chain is confiigured in the `.env` file in the same folder and is pre-configured for Ganache as started by `docker-compose.yml`.
    ```
    npm install
    npm run deploy
    ```

## Configuration
The contract address must be made available to the application server. To do this, configure the address in `src/main/java/resources/configuration/config.properties`.
Alternatively, the address may be set n the environment with the key `ADDRESS_ATTESTATION`, the RPC provider host and port by `PROVIDER_HOST` and `PROVIDER_PORT` respectively. These can also be set inside the `docker-compose.yml` file as follows in the service definition of `webprotege-attestation`.

```yaml
environment:
      - webprotege.mongodb.host=wpmongo
      - ADDRESS_ATTESTATION=<the smart contract address>
      - PROVIDER_HOST=<host name of the RPC endpoint provider>
      - PROVIDER_PORT=<port of the RPC endpoint provider>  
```

## Dataset & Running the tests
The evaluation dataset and test results can be found in the folder `dataset/`.
The tests require the ontologies to be placed in `src/test/resources/ontologies`. 

Configuration of the test blockchain provider are located in `src/test/resources/configuration`. The tests use the JUnit 
framework and can thus be run manually by an appropriate runner, e.g., through Intellij.
