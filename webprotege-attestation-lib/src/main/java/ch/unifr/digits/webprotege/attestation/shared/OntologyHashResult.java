package ch.unifr.digits.webprotege.attestation.shared;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;

public class OntologyHashResult implements Result {
    private String hash;
    private List<Integer> classHashes;
    private int entitySetHash;

    public OntologyHashResult() {}

    public OntologyHashResult(String hash, int entitySetHash, List<Integer> classHashes) {
        this.hash = hash;
        this.entitySetHash = entitySetHash;
        this.classHashes = classHashes;
    }

    public String getHash() {
        return hash;
    }

    public List<Integer> getClassHashes() {
        return classHashes;
    }

    public int getEntitySetHash() {
        return entitySetHash;
    }
}
