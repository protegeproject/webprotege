package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-08
 */
public interface OntologyIndex {


    /**
     * Gets the ontology with the specified ontology id.
     * @param ontologyId The ontology id.
     */
    @Nonnull
    Optional<OWLOntology> getOntology(@Nonnull OWLOntologyID ontologyId);
}
