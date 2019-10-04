package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.impl.DependentIndex;
import edu.stanford.bmir.protege.web.server.index.impl.Index;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ProjectSingleton
public class DefaultOntologyIdManagerImpl implements DefaultOntologyIdManager, DependentIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Inject
    public DefaultOntologyIdManagerImpl(ProjectOntologiesIndex projectOntologiesIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(projectOntologiesIndex);
    }

    @Nonnull
    @Override
    public OWLOntologyID getDefaultOntologyId() {
        Stream<OWLOntologyID> ontologyIds = projectOntologiesIndex.getOntologyIds();
        return ontologyIds.findFirst().orElseThrow();
    }
}
