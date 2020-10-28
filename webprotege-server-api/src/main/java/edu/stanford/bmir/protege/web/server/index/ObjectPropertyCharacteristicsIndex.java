package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyCharacteristic;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public interface ObjectPropertyCharacteristicsIndex extends Index {

    boolean hasCharacteristic(@Nonnull OWLObjectProperty property,
                              @Nonnull ObjectPropertyCharacteristic objectPropertyCharacteristic,
                              @Nonnull OntologyDocumentId ontologyDocumentId);
}
