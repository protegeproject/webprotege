package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public interface DataPropertyCharacteristicsIndex extends Index {

    boolean isFunctional(@Nonnull OWLDataProperty dataProperty,
                         @Nonnull OntologyDocumentId ontologyDocumentId);
}
