package ch.unifr.digits.webprotege.attestation.server.compression.tree;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CompressionTree {

    private String root;

    private HashMap<String, String> subject_layer;
    private HashMap<String, ArrayList<PredicateTree>> predicate_tree_layer;

    private HashMap<String, HashMap<String, ArrayList<String>>> adjacency_lists;

    private int nb_triples;
    private int initial_capacity;

    private RDFTriple[] triples;

    public CompressionTree(RDFTriple[] triples) {
        nb_triples = triples.length;
        initial_capacity = (int) ((0.2 * (double) nb_triples) / 0.75);
        this.triples = triples;

        adjacency_lists = new HashMap<>(initial_capacity);
        subject_layer = new HashMap<>(initial_capacity);
        predicate_tree_layer = new HashMap<>(initial_capacity);

        generateAdjacencyLists();
        generateHashedAdjacencyLists();
        generateCompressionTree();
        deleteAdjacencyLists();
    }

    public CompressionTree(RDFTriple[] triples, boolean empty) {
        nb_triples = triples.length;
        initial_capacity = (int) ((0.2 * (double) nb_triples) / 0.75);
        this.triples = triples;

        adjacency_lists = new HashMap<>(initial_capacity);
        subject_layer = new HashMap<>(initial_capacity);
        predicate_tree_layer = new HashMap<>(initial_capacity);

        if (!empty) {
            generateAdjacencyLists();
            generateHashedAdjacencyLists();
            generateCompressionTree();
            deleteAdjacencyLists();
        }
    }

    public void deleteAdjacencyLists() {
        adjacency_lists = null;
    }

    public void generateCompressionTree() {
        for (String subject: adjacency_lists.keySet()) {

            HashMap<String, ArrayList<String>> predicate_object_list_map = adjacency_lists.get(subject);

            ArrayList<PredicateTree> predicateTrees = new ArrayList<>();

            for (String predicate: predicate_object_list_map.keySet()) {
                // Update predicate trees
                ObjectList objectList = new ObjectList(predicate_object_list_map.get(predicate));
                PredicateTree predicateTree = new PredicateTree(predicate, objectList);
                predicateTrees.add(predicateTree);
            }

            predicate_tree_layer.put(subject, predicateTrees);

            String subject_children_hash = getPredicateTreeDigest(predicate_tree_layer.get(subject));
            subject_layer.put(subject, subject_children_hash);
        }

        updateRoot();
    }

    public void insertTriples(RDFTriple[] triples) {
        for (int i = 0; i < triples.length; i++) {
            insertTriple(triples[i]);
        }
    }

    public void insertHashedTriples(RDFTriple[] triples) {
        for (int i = 0; i < triples.length; i++) {
            insertHashedTriple(triples[i]);
        }
    }

    public void insertTriple(RDFTriple triple) {
        insertTriple(triple.getSubject(), triple.getPredicate(), triple.getObject());
    }

    public void insertHashedTriple(RDFTriple hashed_triple) {
        insertHashedTriple(hashed_triple.getSubject(), hashed_triple.getPredicate(), hashed_triple.getObject());
    }

    public void insertTriple(String subject, String predicate, String object) {
        String hashed_subject = DigestUtils.sha256Hex(subject);
        String hashed_predicate = DigestUtils.sha256Hex(predicate);
        String hashed_object = DigestUtils.sha256Hex(object);

        insert(hashed_subject, hashed_predicate, hashed_object);
    }

    public void insertHashedTriple(String hashed_subject, String hashed_predicate, String hashed_object) {
        insert(hashed_subject, hashed_predicate, hashed_object);
    }

    private void insert(String hashed_subject, String hashed_predicate, String hashed_object) {
        boolean subject_exists = subject_layer.containsKey(hashed_subject);

        if (!subject_exists) {
            ObjectList objectList = new ObjectList(hashed_object);
            PredicateTree predicateTree = new PredicateTree(hashed_predicate, objectList);
            ArrayList<PredicateTree> predicateTrees = new ArrayList<>();
            predicateTrees.add(predicateTree);
            predicate_tree_layer.put(hashed_subject, predicateTrees);
        } else {
            boolean predicate_exists = predicateExists(hashed_subject, hashed_predicate);

            if (!predicate_exists) {
                ObjectList objectList = new ObjectList(hashed_object);
                PredicateTree predicateTree = new PredicateTree(hashed_predicate, objectList);
                predicate_tree_layer.get(hashed_subject).add(predicateTree);
            } else {
                PredicateTree predicateTree = getPredicateTree(hashed_subject, hashed_predicate);
                boolean object_exists = predicateTree.getObject_list().contains(hashed_object);

                if (!object_exists) {
                    predicateTree.getObject_list().getList().add(hashed_object);
                    predicateTree.updateChildrenHash();
                }
            }
        }

        String predicate_tree_digest = getPredicateTreeDigest(predicate_tree_layer.get(hashed_subject));
        subject_layer.put(hashed_subject, predicate_tree_digest);

        updateRoot();
    }

    private PredicateTree getPredicateTree(String hashed_subject, String hashed_predicate) {
        for (PredicateTree predicateTree: predicate_tree_layer.get(hashed_subject)) {
            if (predicateTree.getPredicate().equals(hashed_predicate)) {
                return predicateTree;
            }
        }

        return null;
    }

    private boolean predicateExists(String hashed_subject, String hashed_predicate) {
        for (PredicateTree predicateTree: predicate_tree_layer.get(hashed_subject)) {
            if (predicateTree.getPredicate().equals(hashed_predicate)) {
                return true;
            }
        }

        return false;
    }

    private void updateRoot() {
        root = getDigest(subject_layer);
    }

    private String getPredicateTreeDigest(ArrayList<PredicateTree> predicateTrees) {
        StringBuilder sb = new StringBuilder();

        for (PredicateTree predicateTree: predicateTrees) {
            sb.append(predicateTree.getDigest());
        }

        return DigestUtils.sha256Hex(sb.toString());
    }

    public void generateAdjacencyLists() {
        generateAdjacencyLists(this.triples);
    }

    public void generateAdjacencyLists(RDFTriple[] triples) {
        for (RDFTriple triple: triples) {
            String subject = triple.getSubject();
            String predicate = triple.getPredicate();
            String object = triple.getObject();

            HashMap<String, ArrayList<String>> predicate_object_list_map = adjacency_lists.get(subject);

            if (predicate_object_list_map == null) {
                adjacency_lists.put(subject, createPredicateObjectListMap(predicate, object));
            } else {
                ArrayList<String> object_list = predicate_object_list_map.get(predicate);

                if (object_list == null) {
                    predicate_object_list_map.put(predicate, createObjectList(object));
                } else {
                    boolean object_exists = object_list.contains(object);

                    if (!object_exists) {
                        object_list.add(object);
                    }
                }
            }
        }
    }

    public void generateHashedAdjacencyLists() {
        HashMap<String, HashMap<String, ArrayList<String>>> hashed_adjacency_list = new HashMap<>();

        for (String subject: adjacency_lists.keySet()) {
            HashMap<String, ArrayList<String>> hashed_predicate_map = new HashMap<>();

            for (String predicate: adjacency_lists.get(subject).keySet()) {
                ArrayList<String> hashed_object_list = new ArrayList<>();

                for (String object: adjacency_lists.get(subject).get(predicate)) {
                    hashed_object_list.add(DigestUtils.sha256Hex(object));
                }

                hashed_predicate_map.put(DigestUtils.sha256Hex(predicate), hashed_object_list);
            }

            hashed_adjacency_list.put(DigestUtils.sha256Hex(subject), hashed_predicate_map);
        }

        adjacency_lists = hashed_adjacency_list;
    }

    private HashMap<String, ArrayList<String>> createPredicateObjectListMap(String p, String o) {
        ArrayList<String> object_list = new ArrayList<>();
        object_list.add(o);

        HashMap<String, ArrayList<String>> predicate_tree = new HashMap<>();
        predicate_tree.put(p, object_list);

        return predicate_tree;
    }

    private ArrayList<String> createObjectList(String o) {
        ArrayList<String> object_list = new ArrayList<>();
        object_list.add(o);
        return object_list;
    }

    private String getDigest(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();

        for (String key: map.keySet()) {
            String digest = DigestUtils.sha256Hex(key+map.get(key));
            sb.append(digest);
        }

        return DigestUtils.sha256Hex(sb.toString());
    }


    private String getDigest(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();

        for (String element : list) {
            sb.append(element);
        }

        return DigestUtils.sha256Hex(sb.toString());
    }

    private String getDigest(Set<String> set) {
        StringBuilder sb = new StringBuilder();

        for (String element : set) {
            sb.append(element);
        }

        return DigestUtils.sha256Hex(sb.toString());
    }

    private String getHashTupleListDigest(ArrayList<HashTuple> hashTuples) {
        StringBuilder sb = new StringBuilder();

        for (HashTuple hashTuple: hashTuples) {
            String digest = DigestUtils.sha256Hex(hashTuple.getElementHash()+hashTuple.getChildrenHash());
            sb.append(digest);
        }

        return DigestUtils.sha256Hex(sb.toString());
    }

    public int numberOfUniqueSubjects() {
        return subject_layer.size();
    }

    public int numberOfUniquePredicates() {
        int number_of_unique_predicates = 0;
        for (String subject: predicate_tree_layer.keySet()) {
            number_of_unique_predicates += predicate_tree_layer.get(subject).size();
        }

        return number_of_unique_predicates;
    }

    public int numberOfUniqueObjects() {
        int number_of_unique_objects = 0;
        for (String subject: predicate_tree_layer.keySet()) {
            for (PredicateTree predicate_tree: predicate_tree_layer.get(subject)) {
                number_of_unique_objects += predicate_tree.getObject_list().getList().size();
            }
        }
        return number_of_unique_objects;
    }

    public float averageNumberOfPredicatesPerSubject() {
        return (float) numberOfUniquePredicates()/ (float) numberOfUniqueSubjects();
    }

    public float averageNumberOfObjectsPerPredicate() {
        return (float) numberOfUniqueObjects()/ (float) numberOfUniquePredicates();
    }

    public String getRoot() {
        return root;
    }

    private boolean containsHashedTriple(RDFTriple triple) {
        String subject = triple.getSubject();
        String predicate = triple.getPredicate();
        String object = triple.getObject();

        boolean subject_exists = subject_layer.containsKey(subject);

        if (subject_exists) {
            ArrayList<PredicateTree> predicateTrees = predicate_tree_layer.get(subject);

            for (PredicateTree predicateTree: predicateTrees) {
                if (predicateTree.getPredicate().equals(predicate)) {
                    return predicateTree.getObject_list().getList().contains(object);
                }
            }
        }

        return false;
    }

    public Path getPathForTriple(RDFTriple triple) {
        String subject = triple.getSubject();
        String predicate = triple.getPredicate();
        String object = triple.getObject();

        RDFTriple hashed_triple = new RDFTriple(DigestUtils.sha256Hex(subject), DigestUtils.sha256Hex(predicate), DigestUtils.sha256Hex(object));
        return getPath(hashed_triple);
    }

    public ArrayList<RDFTriple> get(String subject, String predicate, String object) {
        if (subject == null && predicate == null && object == null) {
            return null;
        } else if (subject != null && predicate == null && object == null) {
            return getTriplesUsingSubject(subject);
        } else if (subject != null && predicate == null && object != null) {
            return getTriplesUsingSubjectAndObject(subject, object);
        } else if (subject != null && predicate != null && object == null) {
            return getTriplesUsingSubjectAndPredicate(subject, predicate);
        } else if (subject == null && predicate != null && object == null) {
            return getTriplesUsingPredicate(predicate);
        } else if (subject == null && predicate != null && object != null) {
            return getTriplesUsingPredicateAndObject(predicate, object);
        } else if (subject == null && predicate == null && object != null) {
            return getTriplesUsingObject(object);
        }

        ArrayList<RDFTriple> triples = new ArrayList<>();
        RDFTriple triple = new RDFTriple(subject, predicate, object);
        triples.add(triple);
        return triples;
    }

    private ArrayList<RDFTriple> getTriplesUsingSubjectAndObject(String subject, String object) {
        ArrayList<PredicateTree> predicateTrees = predicate_tree_layer.get(subject);

        if (predicateTrees != null) {
            ArrayList<RDFTriple> triples = new ArrayList<>();

            for (PredicateTree predicateTree: predicateTrees) {
                String predicate = predicateTree.getPredicate();

                for (String current_object: predicateTree.getObject_list().getList()) {
                    if (current_object.equals(object)) {
                        RDFTriple triple = new RDFTriple(subject, predicate, object);
                        triples.add(triple);
                        break;
                    }
                }
            }

            return triples;
        }

        return null;
    }

    private ArrayList<RDFTriple> getTriplesUsingObject(String object) {
        ArrayList<RDFTriple> triples = new ArrayList<>();

        for (String subject: predicate_tree_layer.keySet()) {
            for (PredicateTree predicateTree: predicate_tree_layer.get(subject)) {
                for (String current_object: predicateTree.getObject_list().getList()) {
                    if (current_object.equals(object)) {
                        RDFTriple triple = new RDFTriple(subject, predicateTree.getPredicate(), object);
                        triples.add(triple);
                        break;
                    }
                }
            }
        }

        return triples;
    }

    private ArrayList<RDFTriple> getTriplesUsingPredicateAndObject(String predicate, String object) {
        ArrayList<RDFTriple> triples = new ArrayList<>();

        for (String subject: predicate_tree_layer.keySet()) {
            for (PredicateTree predicateTree: predicate_tree_layer.get(subject)) {
                String current_predicate = predicateTree.getPredicate();

                if (current_predicate.equals(predicate)) {
                    for (String current_object: predicateTree.getObject_list().getList()) {
                        if (current_object.equals(object)) {
                            RDFTriple triple = new RDFTriple(subject, predicate, object);
                            triples.add(triple);
                            break;
                        }
                    }
                    break;
                }
            }
        }

        return triples;
    }

    private ArrayList<RDFTriple> getTriplesUsingPredicate(String predicate) {
        ArrayList<RDFTriple> triples = new ArrayList<>();

        for (String subject: predicate_tree_layer.keySet()) {
            for (PredicateTree predicateTree: predicate_tree_layer.get(subject)) {
                String current_predicate = predicateTree.getPredicate();

                if (current_predicate.equals(predicate)) {
                    for (String object: predicateTree.getObject_list().getList()) {
                        RDFTriple triple = new RDFTriple(subject, predicate, object);
                        triples.add(triple);
                    }
                    break;
                }
            }
        }

        return triples;
    }

    private ArrayList<RDFTriple> getTriplesUsingSubjectAndPredicate(String subject, String predicate) {
        ArrayList<PredicateTree> predicateTrees = predicate_tree_layer.get(subject);

        if (predicateTrees != null) {
            ArrayList<RDFTriple> triples = new ArrayList<>();

            for (PredicateTree predicateTree: predicateTrees) {
                String current_predicate = predicateTree.getPredicate();

                if (predicate.equals(current_predicate)) {
                    for (String object : predicateTree.getObject_list().getList()) {
                        RDFTriple triple = new RDFTriple(subject, predicate, object);
                        triples.add(triple);
                    }

                    return triples;
                }
            }
        }

        return null;
    }

    private ArrayList<RDFTriple> getTriplesUsingSubject(String subject) {
        ArrayList<PredicateTree> predicateTrees = predicate_tree_layer.get(subject);

        if (predicateTrees != null) {
            ArrayList<RDFTriple> triples = new ArrayList<>();

            for (PredicateTree predicateTree: predicateTrees) {
                String predicate = predicateTree.getPredicate();

                for (String object: predicateTree.getObject_list().getList()) {
                    RDFTriple triple = new RDFTriple(subject, predicate, object);
                    triples.add(triple);
                }
            }

            return triples;
        }

        return null;
    }

    public Path getPathForHashedTriple(RDFTriple triple) {
        return getPath(triple);
    }

    private Path getPath(RDFTriple triple) {
        String subject = triple.getSubject();
        String predicate = triple.getPredicate();
        String object = triple.getObject();

        if (containsHashedTriple(triple)) {
            ArrayList<String> subject_list = new ArrayList<>(subject_layer.keySet());
            ArrayList<String> subject_children_hashes = new ArrayList<>(subject_layer.values());
            int index_of_subject = subject_list.indexOf(subject);
            subject_list.remove(subject);
            subject_children_hashes.remove(index_of_subject);

            ArrayList<HashTuple> subject_siblings = new ArrayList<>();

            for (int i = 0; i < subject_list.size(); i++) {
                subject_siblings.add(new HashTuple(subject_list.get(i), subject_children_hashes.get(i)));
            }

            ArrayList<String> predicate_list = new ArrayList<>();
            ArrayList<String> predicate_children_hashes = new ArrayList<>();
            ArrayList<String> object_list = null;

            for (PredicateTree predicate_tree: predicate_tree_layer.get(subject)) {
                predicate_list.add(predicate_tree.getPredicate());
                predicate_children_hashes.add(predicate_tree.getChildren_hash());
                if (predicate_tree.getPredicate().equals(predicate)) {
                    object_list = predicate_tree.getObject_list().getList();
                }
            }

            int index_of_predicate = predicate_list.indexOf(predicate);
            predicate_list.remove(predicate);
            predicate_children_hashes.remove(index_of_predicate);

            ArrayList<HashTuple> predicate_siblings = new ArrayList<>();
            for (int i = 0; i < predicate_list.size(); i++) {
                predicate_siblings.add(new HashTuple(predicate_list.get(i), predicate_children_hashes.get(i)));
            }

            int index_of_object = object_list.indexOf(object);
            object_list.remove(object);

            return new Path(subject_siblings, predicate_siblings, object_list, index_of_subject, index_of_predicate, index_of_object);
        }

        return null;
    }

    public boolean doesTripleExist(RDFTriple triple, String root, Path path) {
        String subject = triple.getSubject();
        String predicate = triple.getPredicate();
        String object = triple.getObject();

        RDFTriple hashed_triple = new RDFTriple(DigestUtils.sha256Hex(subject), DigestUtils.sha256Hex(predicate), DigestUtils.sha256Hex(object));
        return doesHashedTripleExist(hashed_triple, root, path);
    }

    public boolean doesHashedTripleExist(RDFTriple triple, String root, Path path) {
        String subject = triple.getSubject();
        String predicate = triple.getPredicate();
        String object = triple.getObject();

        ArrayList<String> object_list = path.getObject_siblings();
        ArrayList<HashTuple> predicate_list = path.getPredicate_siblings();
        ArrayList<HashTuple> subject_list = path.getSubject_siblings();

        object_list.add(path.getObject_index(), object);
        String predicate_children_hash = getDigest(object_list);
        predicate_list.add(path.getPredicate_index(), new HashTuple(predicate, predicate_children_hash));

        String subject_children_hash = getHashTupleListDigest(predicate_list);
        subject_list.add(path.getSubject_index(), new HashTuple(subject, subject_children_hash));

        String reconstructed_root = getHashTupleListDigest(subject_list);

        return root.equals(reconstructed_root);
    }

    public void printAdjacencyList() {
        for (String subject: adjacency_lists.keySet()) {
            System.out.println("Subject: " + subject);

            for (String predicate: adjacency_lists.get(subject).keySet()) {
                System.out.println("\t\tPredicate: " + predicate);

                for (String object: adjacency_lists.get(subject).get(predicate)) {
                    System.out.println("\t\t\t\tObject: " + object);
                }
            }
        }
    }

    public void printTree(int start, int end) {
        if (start == -1 || start > subject_layer.keySet().size()) {
            start = subject_layer.keySet().size()-1;
        }

        if (end == -1 || end > subject_layer.keySet().size()) {
            end = subject_layer.keySet().size()-1;
        }

        int index = 0;
        System.out.println("Root: " + root);
        for (String subject: subject_layer.keySet()) {
            if (index >= start) {
                printSubjectTree(subject, index);
            }
            if (index == end) {
                break;
            }
            index++;
        }
    }

    public void printSubjectTree(String subject, int index) {
        System.out.println("Subject " + index + ": (" + subject + ", " + subject_layer.get(subject) + ")");

        for (int i = 0; i < predicate_tree_layer.get(subject).size(); i++) {
            predicate_tree_layer.get(subject).get(i).print(2, i);
        }
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public HashMap<String, String> getSubject_layer() {
        return subject_layer;
    }

    public void setSubject_layer(HashMap<String, String> subject_layer) {
        this.subject_layer = subject_layer;
    }

    public HashMap<String, ArrayList<PredicateTree>> getPredicate_tree_layer() {
        return predicate_tree_layer;
    }

    public void setPredicate_tree_layer(HashMap<String, ArrayList<PredicateTree>> predicate_tree_layer) {
        this.predicate_tree_layer = predicate_tree_layer;
    }

    public RDFTriple[] getTriples() {
        return triples;
    }

    public double getSubjectToTriplesRatio() {
        return (double) numberOfUniqueSubjects()/ (double) nb_triples;
    }

    public int getInitial_capacity() {
        return initial_capacity;
    }

    public int getNumberOfRedundantTriples() {
        int i = 9;

        for (String subject: predicate_tree_layer.keySet()) {
            for (PredicateTree predicate_tree: predicate_tree_layer.get(subject)) {
                i+=predicate_tree.getObject_list().getList().size()-1;
            }
        }

        return i;
    }
}
