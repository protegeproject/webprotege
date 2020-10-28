package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-05
 */
public interface OntologyAxiomsSignatureIndex extends Index {

    boolean containsEntityInOntologyAxiomsSignature(@Nonnull OWLEntity entity,
                                                    @Nonnull OntologyDocumentId ontologyDocumentId);

    <E extends OWLEntity> Stream<E> getOntologyAxiomsSignature(@Nonnull EntityType<E> type,
                                                               @Nonnull OntologyDocumentId ontologyDocumentId);
}
