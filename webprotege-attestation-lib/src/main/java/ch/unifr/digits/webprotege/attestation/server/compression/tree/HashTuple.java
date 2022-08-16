package ch.unifr.digits.webprotege.attestation.server.compression.tree;

public class HashTuple {
    private String elementHash;
    private String childrenHash;

    public HashTuple(String elementHash, String childrenHash) {
        this.elementHash = elementHash;
        this.childrenHash = childrenHash;
    }

    public String getElementHash() {
        return elementHash;
    }

    public void setElementHash(String elementHash) {
        this.elementHash = elementHash;
    }

    public String getChildrenHash() {
        return childrenHash;
    }

    public void setChildrenHash(String childrenHash) {
        this.childrenHash = childrenHash;
    }

    @Override
    public String toString() {
        return "implementation.HashTuple{" +
                "elementHash='" + elementHash + '\'' +
                ", childrenHash='" + childrenHash + '\'' +
                '}';
    }
}
