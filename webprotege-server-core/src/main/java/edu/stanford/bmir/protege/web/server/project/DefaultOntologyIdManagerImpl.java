package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ProjectSingleton
public class DefaultOntologyIdManagerImpl implements DefaultOntologyIdManager {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Inject
    public DefaultOntologyIdManagerImpl(ProjectOntologiesIndex projectOntologiesIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
    }

    @Nonnull
    @Override
    public OWLOntologyID getDefaultOntologyId() {
        return projectOntologiesIndex.getOntologyIds().iterator().next();
    }
}
