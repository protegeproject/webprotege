package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
public interface OntologyChangeFactory {

    @Nonnull
    AddAxiom createAddAxiom(@Nonnull OWLOntologyID ontologyID,
                            @Nonnull OWLAxiom axiom);

    @Nonnull
    RemoveAxiom createRemoveAxiom(@Nonnull OWLOntologyID ontologyId,
                                  @Nonnull OWLAxiom axiom);

    @Nonnull
    AddOntologyAnnotation createAddOntologyAnnotation(@Nonnull OWLOntologyID ontologyId,
                                                    @Nonnull OWLAnnotation annotation);

    @Nonnull
    RemoveOntologyAnnotation createRemoveOntologyAnnotation(@Nonnull OWLOntologyID ontologyId,
                                             @Nonnull OWLAnnotation annotation);
}
