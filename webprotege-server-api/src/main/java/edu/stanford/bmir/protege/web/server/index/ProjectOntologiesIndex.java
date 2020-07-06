package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@ProjectSingleton
public interface ProjectOntologiesIndex extends Index {

    /**
     * Gets the ontology Ids of project ontologies
     * @return A stream of ids that represent the ids of project ontologies
     */
    @Nonnull
    Stream<OWLOntologyID> getOntologyIds();
}
