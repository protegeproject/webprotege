package ch.unifr.digits.webprotege.attestation.server.compression.tree;

import java.util.ArrayList;

public class Path {

    private ArrayList<HashTuple> subject_siblings;
    private ArrayList<HashTuple> predicate_siblings;
    private ArrayList<String> object_siblings;

    private int subject_index;
    private int predicate_index;
    private int object_index;

    public Path(ArrayList<HashTuple> subject_siblings, ArrayList<HashTuple> predicate_siblings, ArrayList<String> object_siblings, int subject_index, int predicate_index, int object_index) {
        this.subject_siblings = subject_siblings;
        this.predicate_siblings = predicate_siblings;
        this.object_siblings = object_siblings;
        this.subject_index = subject_index;
        this.predicate_index = predicate_index;
        this.object_index = object_index;
    }

    public ArrayList<String> getObject_siblings() {
        return object_siblings;
    }

    public int getObject_index() {
        return object_index;
    }

    public int getPredicate_index() {
        return predicate_index;
    }

    public int getSubject_index() {
        return subject_index;
    }

    public ArrayList<HashTuple> getSubject_siblings() {
        return subject_siblings;
    }

    public ArrayList<HashTuple> getPredicate_siblings() {
        return predicate_siblings;
    }

    @Override
    public String toString() {
        return "implementation.Path{" +
                "subject_siblings=" + subject_siblings +
                ", predicate_siblings=" + predicate_siblings +
                ", object_siblings=" + object_siblings +
                ", subject_index=" + subject_index +
                ", predicate_index=" + predicate_index +
                ", object_index=" + object_index +
                '}';
    }
}
