package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@ProjectSingleton
public interface OntologyAnnotationsIndex extends Index {

    /**
     * Gets the ontology annotations for the specified ontology Id
     * @param ontologyID The ontology Id
     * @return A stream of annotations that are annotations on the ontology id.  If the ontology Id
     * is not recognized then an empty stream is returned.
     */
    @Nonnull
    Stream<OWLAnnotation> getOntologyAnnotations(@Nonnull OWLOntologyID ontologyID);

    boolean containsAnnotation(@Nonnull OWLAnnotation annotation,
                               @Nonnull OWLOntologyID ontologyId);
}
