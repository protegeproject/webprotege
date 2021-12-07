package ch.unifr.digits.webprotege.attestation.server;

import ch.unifr.digits.webprotege.attestation.server.contracts.ChangeTracking;
import ch.unifr.digits.webprotege.attestation.server.contracts.OntologyAttestation;
import ch.unifr.digits.webprotege.attestation.shared.VerifyResult;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ch.unifr.digits.webprotege.attestation.server.SettingsManager.ADDRESS_CHANGETRACKING;

public class ChangeTrackingAttestationService extends OntologyAttestationService<ChangeTrackingAttestationService.EntitySet> {

    @Override
    public String contractAddress() {
        return ADDRESS_CHANGETRACKING;
    }

    public VerifyResult verifyEntity(String iri, String versionIri, String entityHash) throws Exception {
        ChangeTracking contract = ChangeTracking.load(contractAddress(), WEB3_REF.get(), CREDENTIALS,
                DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
        RemoteFunctionCall<Tuple4<Boolean, String, String, BigInteger>> remoteFunctionCall =
                contract.verifyEntity(iri, versionIri, new BigInteger(entityHash));
        Tuple4<Boolean, String, String, BigInteger> result = remoteFunctionCall.send();
        BigInteger bigInteger = result.component4();
        int time = (bigInteger == null) ? -1 : bigInteger.intValue();
        return new VerifyResult(result.component1(), result.component2(), result.component3(), time);
    }

    @Override
    public VerifyResult verify(String iri, String versionIri, String hash, EntitySet params) throws Exception {
        ChangeTracking contract = ChangeTracking.load(contractAddress(), WEB3_REF.get(), CREDENTIALS,
                DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT);
        RemoteFunctionCall<Tuple4<Boolean, String, String, BigInteger>> remoteFunctionCall =
                contract.verify(iri, versionIri, hash);
        Tuple4<Boolean, String, String, BigInteger> result = remoteFunctionCall.send();
        BigInteger bigInteger = result.component4();
        int time = (bigInteger == null) ? -1 : bigInteger.intValue();
        return new VerifyResult(result.component1(), result.component2(), result.component3(), time);
    }

    @Override
    public TransactionReceipt attest(OWLOntology ontology, String name) throws Exception {
        String hash = ontologyHash(ontology);
        String ontologyIri = ontology.getOntologyID().getOntologyIRI().get().toString();
        String versionIri = ontology.getOntologyID().getVersionIRI().transform(IRI::toString).or("");
        List<BigInteger> classHashes = toBigInt(classHashes(ontology));
        return attest(ontologyIri, versionIri, name, hash, new EntitySet(classHashes));
    }

    @Override
    public TransactionReceipt attest(String iri, String versionIri, String name, String hash, EntitySet params) throws Exception {
        ChangeTracking contract = ChangeTracking.load(contractAddress(), WEB3_REF.get(), CREDENTIALS,
                DefaultGasProvider.GAS_PRICE, new BigInteger("1000000000"));
        RemoteFunctionCall<TransactionReceipt> call = contract.attest(iri, versionIri, name, hash,
                params.hashes);
        TransactionReceipt transactionReceipt = call.send();
        return transactionReceipt;
    }

    public List<BigInteger> toBigInt(List<Integer> ints) {
        List<BigInteger> classHashes = ints.stream().map(BigInteger::valueOf).collect(Collectors.toList());
        return classHashes;
    }

    public List<Integer> classHashes(OWLOntology ontology) {
        Set<OWLClass> classesInSignature = ontology.getClassesInSignature();
        List<Integer> classHashes = classesInSignature.stream().map(this::entityHash).collect(Collectors.toList());
        return classHashes;
    }

    public static class EntitySet {
        public EntitySet(List<BigInteger> hashes) {
            this.hashes = hashes;
        }
        public final List<BigInteger> hashes;
    }
}
