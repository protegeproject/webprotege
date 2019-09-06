package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@ProjectSingleton
public class ProjectOntologiesIndexImpl implements ProjectOntologiesIndex {

    @Nonnull
    private final OWLOntologyManager projectOntologyManager;

    @Nonnull
    private ImmutableList<OWLOntologyID> cache = ImmutableList.of();

    @Inject
    public ProjectOntologiesIndexImpl(@Nonnull OWLOntology rootOntology) {
        this.projectOntologyManager = checkNotNull(checkNotNull(rootOntology).getOWLOntologyManager());
    }

    @Nonnull
    @Override
    public synchronized Stream<OWLOntologyID> getOntologyIds() {
        if(cache.isEmpty()) {
            cache = projectOntologyManager.getOntologies()
                                          .stream()
                                          .map(OWLOntology::getOntologyID)
                                          .collect(toImmutableList());
        }
        return cache.stream();
    }
}
