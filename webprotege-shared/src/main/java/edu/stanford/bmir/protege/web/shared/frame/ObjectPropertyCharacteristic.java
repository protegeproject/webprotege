package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import java.util.Collections;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/01/15
 */
public enum ObjectPropertyCharacteristic {

    @JsonProperty("Functional")
    FUNCTIONAL("Functional"),

    @JsonProperty("InverseFunctional")
    INVERSE_FUNCTIONAL("Inverse Functional"),

    @JsonProperty("Transitive")
    TRANSITIVE("Transitive"),

    @JsonProperty("Symmetric")
    SYMMETRIC("Symmetric"),

    @JsonProperty("Asymmetric")
    ASYMMETRIC("Asymmetric"),

    @JsonProperty("Reflexive")
    REFLEXIVE("Reflexive"),

    @JsonProperty("Irreflexive")
    IRREFLEXIVE("Irreflexive");

    ObjectPropertyCharacteristic(String displayName) {
        this.displayName = displayName;
    }

    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public <A extends OWLAxiom> A createAxiom(OWLObjectPropertyExpression property, OWLDataFactory dataFactory) {
        return createAxiom(property, Collections.emptySet(), dataFactory);
    }

    @SuppressWarnings("unchecked")
    public <A extends OWLAxiom> A createAxiom(OWLObjectPropertyExpression property, Set<? extends OWLAnnotation> annotations, OWLDataFactory dataFactory) {
        switch(this) {
            case FUNCTIONAL:
                return (A) dataFactory.getOWLFunctionalObjectPropertyAxiom(property, annotations);
            case INVERSE_FUNCTIONAL:
                return (A) dataFactory.getOWLInverseFunctionalObjectPropertyAxiom(property, annotations);
            case SYMMETRIC:
                return (A) dataFactory.getOWLSymmetricObjectPropertyAxiom(property, annotations);
            case ASYMMETRIC:
                return (A) dataFactory.getOWLAsymmetricObjectPropertyAxiom(property, annotations);
            case REFLEXIVE:
                return (A) dataFactory.getOWLReflexiveObjectPropertyAxiom(property, annotations);
            case IRREFLEXIVE:
                return (A) dataFactory.getOWLIrreflexiveObjectPropertyAxiom(property, annotations);
            case TRANSITIVE:
                return (A) dataFactory.getOWLTransitiveObjectPropertyAxiom(property, annotations);
        }
        throw new IllegalStateException("Missing case for property: " + this);
    }
}
