package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.index.OntologyIndex;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
public class OntologyChangeFactoryImpl implements OntologyChangeFactory {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public OntologyChangeFactoryImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Nonnull
    @Override
    public AddAxiom createAddAxiom(@Nonnull OWLOntologyID ontologyID,
                                   @Nonnull OWLAxiom axiom) {
        return getOntology(ontologyID).map(ont -> new AddAxiom(ont, axiom)).orElseThrow();
    }

    @Nonnull
    @Override
    public RemoveAxiom createRemoveAxiom(@Nonnull OWLOntologyID ontologyId,
                                         @Nonnull OWLAxiom axiom) {
        return getOntology(ontologyId).map(ont -> new RemoveAxiom(ont, axiom)).orElseThrow();
    }

    @Nonnull
    @Override
    public AddOntologyAnnotation createAddOntologyAnnotation(@Nonnull OWLOntologyID ontologyId,
                                                                @Nonnull OWLAnnotation annotation) {
        return getOntology(ontologyId).map(ont -> new AddOntologyAnnotation(ont, annotation)).orElseThrow();
    }

    @Nonnull
    @Override
    public RemoveOntologyAnnotation createRemoveOntologyAnnotation(@Nonnull OWLOntologyID ontologyId,
                                                                   @Nonnull OWLAnnotation annotation) {
        return getOntology(ontologyId).map(ont -> new RemoveOntologyAnnotation(ont, annotation)).orElseThrow();
    }


    private Optional<OWLOntology> getOntology(@Nonnull OWLOntologyID ontologyId) {
        return ontologyIndex.getOntology(ontologyId);
    }

}
