package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@ProjectSingleton
public interface ProjectOntologiesIndex {

    /**
     * Gets the ontology Ids of project ontologies
     * @return A stream of ids that represent the ids of project ontologies
     */
    @Nonnull
    Stream<OWLOntologyID> getOntologyIds();

    /**
     * Gets the ontology with the specified ontology id.
     * @param ontologyId The ontology id.
     */
    @Nonnull
    Optional<OWLOntology> getOntology(@Nonnull OWLOntologyID ontologyId);
}
