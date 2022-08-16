package ch.unifr.digits.webprotege.attestation.server.compression.tree;

import org.apache.commons.codec.digest.DigestUtils;

public class PredicateTree {
    private String predicate;
    private String children_hash;
    private ObjectList object_list;

    public PredicateTree(String predicate, ObjectList object_list) {
        this.predicate = predicate;
        this.children_hash = object_list.getDigest();
        this.object_list = object_list;
    }

    public void updateChildrenHash() {
        children_hash = object_list.getDigest();
    }

    public String getDigest() {
        return DigestUtils.sha256Hex(predicate+children_hash);
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getChildren_hash() {
        return children_hash;
    }

    public void setChildren_hash(String children_hash) {
        this.children_hash = children_hash;
    }

    public ObjectList getObject_list() {
        return object_list;
    }

    public void setObject_list(ObjectList object_list) {
        this.object_list = object_list;
    }

    private String getIndentationString(int nb_indents) {
        StringBuilder sb = new StringBuilder();

        sb.append("\t".repeat(Math.max(0, nb_indents)));

        return sb.toString();
    }

    public void print(int nb_indents, int index) {
        System.out.println(getIndentationString(nb_indents) + "Predicate " + index + ": (" + predicate + ", " + children_hash + ")");
        object_list.print(nb_indents+2);
    }
}
