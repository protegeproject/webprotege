package edu.stanford.bmir.protege.web.shared.axiom;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.AxiomType;

import static org.semanticweb.owlapi.model.AxiomType.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/02/15
 */
public class DefaultAxiomTypeOrdering {

    private static final ImmutableList<AxiomType<?>> DEFAULT_ORDERING;

    static {
        DEFAULT_ORDERING = ImmutableList.<AxiomType<?>>builder()
                .add(
                        DECLARATION,

                        ANNOTATION_ASSERTION,

                        // Class axioms
                        EQUIVALENT_CLASSES,
                        SUBCLASS_OF,
                        DISJOINT_CLASSES,
                        DISJOINT_UNION,
                        HAS_KEY,

                        // Datatype axioms,
                        DATATYPE_DEFINITION,

                        // Object property axioms
                        EQUIVALENT_OBJECT_PROPERTIES,
                        SUB_OBJECT_PROPERTY,
                        SUB_PROPERTY_CHAIN_OF,
                        INVERSE_OBJECT_PROPERTIES,
                        DISJOINT_OBJECT_PROPERTIES,
                        OBJECT_PROPERTY_DOMAIN,
                        OBJECT_PROPERTY_RANGE,
                        FUNCTIONAL_OBJECT_PROPERTY,
                        INVERSE_FUNCTIONAL_OBJECT_PROPERTY,
                        TRANSITIVE_OBJECT_PROPERTY,
                        SYMMETRIC_OBJECT_PROPERTY,
                        ASYMMETRIC_OBJECT_PROPERTY,
                        REFLEXIVE_OBJECT_PROPERTY,
                        IRREFLEXIVE_OBJECT_PROPERTY,

                        // Data property axioms
                        EQUIVALENT_DATA_PROPERTIES,
                        SUB_DATA_PROPERTY,
                        DISJOINT_DATA_PROPERTIES,
                        DATA_PROPERTY_DOMAIN,
                        DATA_PROPERTY_RANGE,
                        FUNCTIONAL_DATA_PROPERTY,

                        // ABox axioms
                        CLASS_ASSERTION,
                        OBJECT_PROPERTY_ASSERTION,
                        DATA_PROPERTY_ASSERTION,
                        SAME_INDIVIDUAL,
                        DIFFERENT_INDIVIDUALS,
                        NEGATIVE_OBJECT_PROPERTY_ASSERTION,
                        NEGATIVE_DATA_PROPERTY_ASSERTION,

                        // Annotation property axioms
                        SUB_ANNOTATION_PROPERTY_OF,
                        ANNOTATION_PROPERTY_DOMAIN,
                        ANNOTATION_PROPERTY_RANGE,

                        // Rules
                        SWRL_RULE


                ).build();
    }

    public static ImmutableList<AxiomType<?>> get() {
        return DEFAULT_ORDERING;
    }
}
