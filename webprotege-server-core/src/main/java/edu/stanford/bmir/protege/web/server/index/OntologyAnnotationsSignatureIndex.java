package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-05
 */
public interface OntologyAnnotationsSignatureIndex {

    boolean containsEntityInSignature(@Nonnull OWLEntity entity,
                                      @Nonnull OWLOntologyID ontologyId);
}
