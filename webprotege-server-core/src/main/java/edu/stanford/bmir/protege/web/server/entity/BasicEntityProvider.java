package edu.stanford.bmir.protege.web.server.entity;

import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.*;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-10-31
 */
public class BasicEntityProvider implements OWLEntityProvider {

    @Nonnull
    @Override
    public OWLAnnotationProperty getOWLAnnotationProperty(@Nonnull IRI iri) {
        return new OWLAnnotationPropertyImpl(iri);
    }

    @Nonnull
    @Override
    public OWLClass getOWLClass(@Nonnull IRI iri) {
        return new OWLClassImpl(iri);
    }

    @Nonnull
    @Override
    public OWLDataProperty getOWLDataProperty(@Nonnull IRI iri) {
        return new OWLDataPropertyImpl(iri);
    }

    @Nonnull
    @Override
    public OWLDatatype getOWLDatatype(@Nonnull IRI iri) {
        return new OWLDatatypeImpl(iri);
    }

    @Nonnull
    @Override
    public OWLNamedIndividual getOWLNamedIndividual(@Nonnull IRI iri) {
        return new OWLNamedIndividualImpl(iri);
    }

    @Nonnull
    @Override
    public OWLObjectProperty getOWLObjectProperty(@Nonnull IRI iri) {
        return new OWLObjectPropertyImpl(iri);
    }
}
