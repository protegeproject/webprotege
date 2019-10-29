package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@ProjectSingleton
public class ProjectOntologiesIndexImpl implements ProjectOntologiesIndex, UpdatableIndex {

    private static Logger logger = LoggerFactory.getLogger(ProjectOntologiesIndexImpl.class);

    @Nonnull
    private final Multiset<OWLOntologyID> ontologyIds = HashMultiset.create();

    @Nonnull
    private ImmutableList<OWLOntologyID> cache = ImmutableList.of();

    private boolean initialized = false;

    @Inject
    public ProjectOntologiesIndexImpl() {
    }

    @Nonnull
    @Override
    public synchronized Stream<OWLOntologyID> getOntologyIds() {
        if(!initialized) {
            throw new RuntimeException("Index not initialized");
        }
        return cache.stream();
    }

    public synchronized void init(RevisionManager revisionManager) {
        if(initialized) {
            return;
        }
        revisionManager.getRevisions()
                       .forEach(rev -> applyChanges(rev.getChanges()));
        initialized = true;
    }

    @Override
    public synchronized void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        for(var ontologyChange : changes) {
            if(ontologyChange.isAddAxiom() || ontologyChange.isAddOntologyAnnotation()) {
                ontologyIds.add(ontologyChange.getOntologyId());
            }
            else if(ontologyChange.isRemoveAxiom() || ontologyChange.isRemoveOntologyAnnotation()) {
                ontologyIds.remove(ontologyChange.getOntologyId());
            }
        }
        cache = ImmutableList.copyOf(ontologyIds.elementSet());
        initialized = true;
    }
}
