package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@ProjectSingleton
public class RootOntologyProvider implements Provider<OWLOntology> {

    @Nonnull
    private final RootOntologyLoader loader;

    private OWLOntology rootOntology = null;

    @Inject
    public RootOntologyProvider(RootOntologyLoader loader) {
        this.loader = checkNotNull(loader);
    }

    @Override
    public synchronized OWLOntology get() {
        if(rootOntology != null) {
            return rootOntology;
        }
        rootOntology = loader.loadRootOntology();
        return rootOntology;
    }


}
