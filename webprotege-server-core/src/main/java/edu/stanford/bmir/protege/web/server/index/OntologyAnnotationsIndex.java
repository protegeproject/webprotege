package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
public interface OntologyAnnotationsIndex {

    /**
     * Gets the ontology annotations for the specified ontology Id
     * @param ontologyID The ontology Id
     * @return A stream of annotations that are annotations on the ontology id
     */
    @Nonnull
    Stream<OWLAnnotation> getOntologyAnnotations(@Nonnull OWLOntologyID ontologyID);
}
