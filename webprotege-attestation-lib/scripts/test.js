const Web3 = require('web3');
const fs = require('fs');
require('dotenv').config({ path: `.env` });

const buildPath = `${__dirname}/../build/`;
const contract_address = '0x41c293160913d7cD816093D702e2dDA7391A3976';

function load(contractName) {
    const abi = fs.readFileSync(`${buildPath}${contractName}.abi`, 'UTF-8');
    const bin = fs.readFileSync(`${buildPath}${contractName}.bin`, 'UTF-8');
    return { abi: JSON.parse(abi), bin: bin };
}

const abi = load('ChangeTracking').abi;
const provider = `${process.env.PROVIDER_HOST}:${process.env.PROVIDER_PORT}`;
console.log(`Using web3 provider: ${provider}`);

const web3 = new Web3(provider);
const account = web3.eth.accounts.privateKeyToAccount(process.env.SERVER_SECRET);
console.log(`Using web3 account: ${account.address}`);
console.log(`Using contract address: ${contract_address}`);

const contract = new web3.eth.Contract(abi, contract_address, {
    from: account.address
});
contract.methods.attest("1", "2", "t", "3", []).send();
contract.methods.verify("1", "2", "3").call();
contract.methods.verifyEntity("1", "2", "3").call();