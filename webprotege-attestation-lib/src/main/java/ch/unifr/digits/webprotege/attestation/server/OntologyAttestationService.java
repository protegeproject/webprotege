package ch.unifr.digits.webprotege.attestation.server;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class OntologyAttestationService<T> extends FileAttestationService<T> {

    public TransactionReceipt attest(OWLOntology ontology, String name) throws Exception {
        String hash = ontologyHash(ontology);
        String ontologyIri = ontology.getOntologyID().getOntologyIRI().get().toString();
        String versionIri = ontology.getOntologyID().getVersionIRI().transform(IRI::toString).or("");
        return attest(ontologyIri, versionIri, name, hash, null);
    }

    public String ontologyHash(OWLOntology ontology) {
        Set<OWLEntity> signature = ontology.getSignature(Imports.INCLUDED);
        Set<OWLAxiom> axioms = ontology.getAxioms(Imports.INCLUDED);
        Set<OWLAnnotation> annotations = ontology.getAnnotations();
        Set<OWLObject> all = new HashSet<>();
        all.addAll(signature);
        all.addAll(annotations);
        all.addAll(axioms);
        return HashStringUtils.boundedHex(all.hashCode());
    }

    public int entityHash(OWLEntity entity) {
        return entity.hashCode();
    }
}
