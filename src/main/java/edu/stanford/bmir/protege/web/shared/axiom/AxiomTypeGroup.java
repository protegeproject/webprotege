package edu.stanford.bmir.protege.web.shared.axiom;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/07/2013
 */
public enum AxiomTypeGroup {

    SUBCLASS_OF("SubClassOf", OWLRDFVocabulary.RDFS_SUBCLASS_OF, AxiomType.SUBCLASS_OF),

    EQUIVALENT_CLASSES("Equivalent Classes", OWLRDFVocabulary.OWL_EQUIVALENT_CLASS, AxiomType.EQUIVALENT_CLASSES),

    DISJOINT_CLASSES("Disjoint Classes", OWLRDFVocabulary.OWL_DISJOINT_WITH, AxiomType.DISJOINT_CLASSES),


    DOMAIN("Domain", OWLRDFVocabulary.RDFS_DOMAIN, AxiomType.OBJECT_PROPERTY_DOMAIN, AxiomType.DATA_PROPERTY_DOMAIN, AxiomType.ANNOTATION_PROPERTY_DOMAIN),

    RANGE("Range", OWLRDFVocabulary.RDFS_RANGE, AxiomType.OBJECT_PROPERTY_RANGE, AxiomType.DATA_PROPERTY_RANGE, AxiomType.ANNOTATION_PROPERTY_RANGE),

    SUB_PROPERTY_OF("SubPropertyOf", OWLRDFVocabulary.RDFS_SUB_PROPERTY_OF, AxiomType.SUB_OBJECT_PROPERTY, AxiomType.SUB_DATA_PROPERTY, AxiomType.SUB_ANNOTATION_PROPERTY_OF, AxiomType.SUB_PROPERTY_CHAIN_OF),

    EQUIVALENT_PROPERTIES("Equivalent Properties", OWLRDFVocabulary.OWL_EQUIVALENT_PROPERTY, AxiomType.EQUIVALENT_OBJECT_PROPERTIES, AxiomType.EQUIVALENT_DATA_PROPERTIES),

    INVERSE_PROPERTIES("Inverse Properties", OWLRDFVocabulary.OWL_INVERSE_OF, AxiomType.INVERSE_OBJECT_PROPERTIES),

    DISJOINT_PROPERTIES("Disjoint Properties", OWLRDFVocabulary.OWL_DISJOINT_WITH, AxiomType.DISJOINT_OBJECT_PROPERTIES, AxiomType.DISJOINT_DATA_PROPERTIES),


    PROPERTY_CHARACTERISTIC("Property Characteristic", AxiomType.FUNCTIONAL_OBJECT_PROPERTY, AxiomType.FUNCTIONAL_DATA_PROPERTY, AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY, AxiomType.SYMMETRIC_OBJECT_PROPERTY, AxiomType.ASYMMETRIC_OBJECT_PROPERTY, AxiomType.TRANSITIVE_OBJECT_PROPERTY, AxiomType.REFLEXIVE_OBJECT_PROPERTY, AxiomType.IRREFLEXIVE_OBJECT_PROPERTY),


    CLASS_ASSERTION("Class Assertion", OWLRDFVocabulary.RDF_TYPE, AxiomType.CLASS_ASSERTION),

    PROPERTY_ASSERTION("Property Assertion", AxiomType.OBJECT_PROPERTY_ASSERTION, AxiomType.DATA_PROPERTY_ASSERTION),

    NEGATIVE_PROPERTY_ASSERTION("Negative Property Assertion", AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION, AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION),

    SAME_INDIVIDUAL("Same Individual", OWLRDFVocabulary.OWL_SAME_AS, AxiomType.SAME_INDIVIDUAL),

    DIFFERENT_INDIVIDUALS("Different Individuals", OWLRDFVocabulary.OWL_DIFFERENT_FROM, AxiomType.DIFFERENT_INDIVIDUALS),

    ANNOTATION_ASSERTION("Annotation Assertion", AxiomType.ANNOTATION_ASSERTION),


    OTHER("Other", AxiomType.DISJOINT_UNION, AxiomType.HAS_KEY, AxiomType.DATATYPE_DEFINITION, AxiomType.SWRL_RULE, AxiomType.DECLARATION);



    private static final Map<AxiomType<?>, AxiomTypeGroup> TYPE_MAP = new HashMap<AxiomType<?>, AxiomTypeGroup>();


    static {
        performIntegrityCheck();
    }





    /**
     * Performs an integrity check to ensure each axiom type belongs to at least one group
     */
    private static void performIntegrityCheck() {
        Set<AxiomType> initialisedType = new HashSet<AxiomType>();
        for(AxiomTypeGroup group : values()) {
            for(AxiomType<?> type : group.getAxiomTypes()) {
                TYPE_MAP.put(type, group);
                initialisedType.add(type);
            }
        }
        for(AxiomType axiomType : AxiomType.AXIOM_TYPES) {
            if(!initialisedType.contains(axiomType)) {
                throw new RuntimeException("Missing Axiom Type: " + axiomType);
            }
        }
    }

    public static AxiomTypeGroup getAxiomTypeGroup(AxiomType<?> axiomType) {
        return TYPE_MAP.get(axiomType);
    }



    private String displayName;

    private Optional<OWLRDFVocabulary> owlrdfVocabulary;

    private final List<AxiomType<?>> axiomTypes;

    AxiomTypeGroup(String displayName, OWLRDFVocabulary vocabulary, AxiomType<?>... axiomTypes) {
        this.displayName = displayName;
        this.owlrdfVocabulary = Optional.of(vocabulary);
        this.axiomTypes = wrapArray(axiomTypes);
    }

    private List<AxiomType<?>> wrapArray(AxiomType<?>[] axiomTypes) {
        return Collections.unmodifiableList(new ArrayList<AxiomType<?>>(Arrays.asList(axiomTypes)));
    }

    AxiomTypeGroup(String displayName, AxiomType... axiomTypes) {
        this.displayName = displayName;
        this.owlrdfVocabulary = Optional.absent();
        this.axiomTypes = wrapArray(axiomTypes);
    }

    public String getDisplayName() {
        return displayName;
    }

    public Optional<OWLRDFVocabulary> getOWLRDFVocabulary() {
        return owlrdfVocabulary;
    }

    public List<AxiomType<?>> getAxiomTypes() {
        return axiomTypes;
    }


}
