package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-05
 */
public interface EntitiesInOntologySignatureByIriIndex extends Index {

    @Nonnull
    Stream<OWLEntity> getEntitiesInSignature(@Nonnull IRI iri,
                                             @Nonnull OntologyDocumentId ontologyDocumentId);
}
