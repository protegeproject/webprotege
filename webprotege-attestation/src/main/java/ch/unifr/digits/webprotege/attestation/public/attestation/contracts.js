function OntologyAttestationContract(web3, jsonInterface, address) {
    this.delegate = new web3.eth.Contract(jsonInterface, address);
    this.verify = function(from, ontologyIri, versionIri, hash) {
        return this.delegate.methods.verify(ontologyIri, versionIri, hash).call({from: from});
    }
    this.attest = function(from, ontologyIri, versionIri, name, hash) {
        return this.delegate.methods.attest(ontologyIri, versionIri, name, hash).send({from: from});
    }
}
function ChangeTrackingContract(web3, jsonInterface, address) {
    this.delegate = new web3.eth.Contract(jsonInterface, address);
    this.verifyEntity = function(from, ontologyIri, versionIri, entityHash) {
        return this.delegate.methods.verifyEntity(ontologyIri, versionIri, entityHash).call({from: from});
    }
    this.verify = function(from, ontologyIri, versionIri, hash) {
        return this.delegate.methods.verify(ontologyIri, versionIri, hash).call({from: from});
    }
    this.attest = function(from, ontologyIri, versionIri, name, hash, classHashes) {
        var array = convertIntList(classHashes);
        console.log(array);
        return this.delegate.methods.attest(ontologyIri, versionIri, name, hash, array).send({from: from});
    }
}
function convertIntList(list) {
    var array = [];
    if ('java_util_ArrayList_array' in list && list['java_util_ArrayList_array']) {
        var intObjArr = list['java_util_ArrayList_array'];
        intObjArr.forEach(intObj => {
            if ('java_lang_Integer_value' in intObj && intObj['java_lang_Integer_value']) {
                var val = intObj['java_lang_Integer_value'];
                array.push(val);
            }
        })
    }
    return array;
}
