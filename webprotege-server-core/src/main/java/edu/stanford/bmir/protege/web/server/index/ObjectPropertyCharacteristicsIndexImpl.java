package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyCharacteristic;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public class ObjectPropertyCharacteristicsIndexImpl implements ObjectPropertyCharacteristicsIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public ObjectPropertyCharacteristicsIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Override
    public boolean hasCharacteristic(@Nonnull OWLObjectProperty property,
                                     @Nonnull ObjectPropertyCharacteristic characteristic,
                                     @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(property);
        checkNotNull(characteristic);
        checkNotNull(ontologyId);
        var ontology = ontologyIndex.getOntology(ontologyId);
        var ontologyStream = ontology.stream();
        switch(characteristic) {
            case FUNCTIONAL:
                return ontologyStream
                        .flatMap(ont -> ont.getFunctionalObjectPropertyAxioms(property).stream())
                        .findFirst()
                        .isPresent();
            case INVERSE_FUNCTIONAL:
                return ontologyStream
                        .flatMap(ont -> ont.getInverseFunctionalObjectPropertyAxioms(property).stream())
                        .findFirst()
                        .isPresent();
            case SYMMETRIC:
                return ontologyStream
                        .flatMap(ont -> ont.getSymmetricObjectPropertyAxioms(property).stream())
                        .findFirst()
                        .isPresent();
            case ASYMMETRIC:
                return ontologyStream
                        .flatMap(ont -> ont.getAsymmetricObjectPropertyAxioms(property).stream())
                        .findFirst()
                        .isPresent();
            case REFLEXIVE:
                return ontologyStream
                        .flatMap(ont -> ont.getReflexiveObjectPropertyAxioms(property).stream())
                        .findFirst()
                        .isPresent();
            case IRREFLEXIVE:
                return ontology
                        .stream()
                        .flatMap(ont -> ont.getIrreflexiveObjectPropertyAxioms(property).stream())
                        .findFirst()
                        .isPresent();
            case TRANSITIVE:
                return ontology
                        .stream()
                        .flatMap(ont -> ont.getTransitiveObjectPropertyAxioms(property).stream())
                        .findFirst()
                        .isPresent();
            default:
                throw new RuntimeException("Unknown characteristic: " + characteristic);
        }
    }
}
