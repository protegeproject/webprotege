// SPDX-License-Identifier: GPL-3.0
pragma solidity >= 0.7.1;

contract OntologyAttestation {

    struct Attestation {
        address signer;
        string name;
        uint timestamp;
        string hash;
    }

    mapping (string => Attestation) internal store;

    function attest(string calldata ontologyIri, string calldata versionIri, string calldata name, string calldata hash) public {
        require(bytes(ontologyIri).length > 0, "IRI required");
        require(bytes(name).length > 0, "Name required");
        require(bytes(hash).length > 0, "Ontology hash required");

        string memory id = getId(ontologyIri, versionIri);
        Attestation memory newAttestation = Attestation(msg.sender, name, block.timestamp, hash);
        store[id] = newAttestation;
    }

    function verify(string calldata ontologyIri, string calldata versionIri, string calldata  hash) public view 
    returns (bool valid, address signer, string memory signerName, uint timestamp) {
        string memory id = getId(ontologyIri, versionIri);
        Attestation memory attestation = store[id];
        return verifyHash(attestation, attestation.hash, hash);
    }

    function verifyHash(Attestation memory attestation, string memory hashExpected, string calldata hashActual) private pure
    returns (bool valid, address signer, string memory signerName, uint timestamp) {
        if (attestation.signer != address(0x0) && hashEqual(hashExpected, hashActual)) {
            return (true, attestation.signer, attestation.name, attestation.timestamp);
        }
        return (false, attestation.signer, "", 0);
    }

    function hashEqual(string memory a, string calldata b) internal pure returns (bool) {
    if(bytes(a).length != bytes(b).length) {
        return false;
    } else {
        return keccak256(abi.encode(a)) == keccak256(abi.encode(b));
    }
}

    function getId(string calldata ontologyIri, string calldata versionIri) internal pure returns(string memory) {
        return string(abi.encodePacked(ontologyIri, versionIri));
    }
}