package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-08
 *
 * A low-level detail index that provides direct access to {@link OWLOntology} objects that
 * are contained in a project.  In general, instances of this interfaces should not be used.
 * Instead, higher level, more fine grained, indexes should be used to access the desired
 * streams of axioms and ontology annotations.
 */
@ProjectSingleton
public interface OntologyIndex {


    /**
     * Gets the ontology with the specified ontology id.
     * @param ontologyId The ontology id.
     */
    @Nonnull
    Optional<OWLOntology> getOntology(@Nonnull OWLOntologyID ontologyId);
}
