package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntologyID;

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

    @Nonnull
    private final Multiset<OWLOntologyID> ontologyIds = HashMultiset.create();

    @Nonnull
    private ImmutableList<OWLOntologyID> cache = ImmutableList.of();

    @Inject
    public ProjectOntologiesIndexImpl() {
    }

    @Nonnull
    @Override
    public synchronized Stream<OWLOntologyID> getOntologyIds() {
        return cache.stream();
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
    }
}
