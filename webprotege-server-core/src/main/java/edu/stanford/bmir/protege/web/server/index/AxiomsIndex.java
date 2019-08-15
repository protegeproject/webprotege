package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
public interface AxiomsIndex {

    boolean containsAxiom(@Nonnull OWLAxiom axiom,
                          @Nonnull OWLOntologyID ontologyId);

    boolean containsAxiomIgnoreAnnotations(@Nonnull OWLAxiom axiom,
                                           @Nonnull OWLOntologyID ontologyId);
}
