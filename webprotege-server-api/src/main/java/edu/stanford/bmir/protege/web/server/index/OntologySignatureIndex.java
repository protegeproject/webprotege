package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

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
     * @param ontologyID The ontologyId.
     * @return A stream of entities that represent the signature of the specified ontology.
     * An empty stream if the ontology is not known.  The stream will not contain duplicate
     * entities.
     */
    @Nonnull
    Stream<OWLEntity> getEntitiesInSignature(@Nonnull OWLOntologyID ontologyID);
}
