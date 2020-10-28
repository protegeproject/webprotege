package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ProjectSingleton
public interface OntologySignatureIndex extends Index {

    /**
     * Gets the entities in the signature of the specified ontology.
     * @param ontologyDocumentId The ontologyId.
     * @return A stream of entities that represent the signature of the specified ontology.
     * An empty stream if the ontology is not known.  The stream will not contain duplicate
     * entities.
     */
    @Nonnull
    Stream<OWLEntity> getEntitiesInSignature(@Nonnull OntologyDocumentId ontologyDocumentId);
}
