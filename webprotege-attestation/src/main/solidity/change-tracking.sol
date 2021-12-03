// SPDX-License-Identifier: GPL-3.0
pragma solidity >= 0.7.1;

contract ChangeTracking {

    struct Attestation {
        address signer;
        string name;
        uint timestamp;
        string hash;
        ClassSignatures classes;
    }

    struct ClassSignatures {
        int[] classHashes;
    }

    mapping (string => Attestation) internal store;

    function attest(string calldata ontologyIri, string calldata versionIri, string calldata name, string calldata hash, int[] calldata classHashes) public {
        require(bytes(ontologyIri).length > 0, "IRI required");
        require(bytes(name).length > 0, "Name required");
        require(bytes(hash).length > 0, "Ontology hash required");

        string memory id = getId(ontologyIri, versionIri);
        ClassSignatures memory classes = ClassSignatures(classHashes);
        Attestation memory newAttestation = Attestation(msg.sender, name, block.timestamp, hash, classes);
        store[id] = newAttestation;
    }

    function verify(string calldata ontologyIri, string calldata versionIri, string calldata hash) public view 
    returns (bool valid, address signer, string memory signerName, uint timestamp) {
        string memory id = getId(ontologyIri, versionIri);
        Attestation memory attestation = store[id];
        if (attestation.signer != address(0x0) && strHashEqual(attestation.hash, hash)) {
            return (true, attestation.signer, attestation.name, attestation.timestamp);
        }
        return (false, address(0x0), "", 0);
    }

    function verifyEntity(string calldata ontologyIri, string calldata versionIri, int entityHash) public view
    returns (bool valid, address signer, string memory signerName, uint timestamp) {
        string memory id = getId(ontologyIri, versionIri);
        Attestation memory attestation = store[id];
        int[] memory array = attestation.classes.classHashes;
        for (uint i = 0; i < array.length; i++ ) {
            if (array[i] == entityHash) return (true, attestation.signer, attestation.name, attestation.timestamp);
        }
        return (false, address(0x0), "", 0);
    }

    function strHashEqual(string memory a, string calldata b) internal pure returns (bool) {
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