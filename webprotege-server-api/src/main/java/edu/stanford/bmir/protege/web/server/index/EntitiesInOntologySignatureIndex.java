package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
public interface EntitiesInOntologySignatureIndex extends Index {

    boolean containsEntityInSignature(@Nonnull OWLEntity entity,
                                      @Nonnull OntologyDocumentId ontologyDocumentId);
}
