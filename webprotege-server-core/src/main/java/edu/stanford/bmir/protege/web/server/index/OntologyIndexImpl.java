package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-08
 */
public class OntologyIndexImpl implements OntologyIndex {

    @Nonnull
    private final OWLOntologyManager projectOntologyManager;

    @Inject
    public OntologyIndexImpl(@Nonnull OWLOntology rootOntology) {
        this.projectOntologyManager = checkNotNull(checkNotNull(rootOntology).getOWLOntologyManager());
    }


    @Nonnull
    @Override
    public Optional<OWLOntology> getOntology(@Nonnull OWLOntologyID ontologyId) {
        return Optional.ofNullable(projectOntologyManager.getOntology(ontologyId));
    }
}
