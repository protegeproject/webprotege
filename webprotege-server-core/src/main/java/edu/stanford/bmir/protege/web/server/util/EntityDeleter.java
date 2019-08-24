package edu.stanford.bmir.protege.web.server.util;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeFactory;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
public class EntityDeleter {

    @Nonnull
    private final ReferenceFinder referenceFinder;

    @Nonnull
    private final OntologyChangeFactory ontologyChangeFactory;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Inject
    public EntityDeleter(@Nonnull ReferenceFinder referenceFinder,
                         @Nonnull OntologyChangeFactory ontologyChangeFactory,
                         @Nonnull ProjectOntologiesIndex projectOntologiesIndex) {
        this.referenceFinder = checkNotNull(referenceFinder);
        this.ontologyChangeFactory = checkNotNull(ontologyChangeFactory);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
    }

    public ImmutableList<OWLOntologyChange> getChangesToDeleteEntities(@Nonnull Collection<OWLEntity> entities) {
        checkNotNull(entities);
        if(entities.isEmpty()) {
            return ImmutableList.of();
        }
        return projectOntologiesIndex.getOntologyIds()
                .flatMap(ontologyID -> getChangesForOntology(entities, ontologyID))
                .collect(toImmutableList());
    }

    private Stream<OWLOntologyChange> getChangesForOntology(Collection<OWLEntity> entities, OWLOntologyID ontology) {
        ReferenceFinder.ReferenceSet referenceSet = referenceFinder.getReferenceSet(entities, ontology);
        List<OWLOntologyChange> changeList = new ArrayList<>(
                referenceSet.getReferencingAxioms().size() + referenceSet.getReferencingOntologyAnnotations().size()
        );
        for(OWLAxiom ax : referenceSet.getReferencingAxioms()) {
            changeList.add(ontologyChangeFactory.createRemoveAxiom(referenceSet.getOntologyId(), ax));
        }
        for(OWLAnnotation annotation : referenceSet.getReferencingOntologyAnnotations()) {
            changeList.add(ontologyChangeFactory.createRemoveOntologyAnnotation(referenceSet.getOntologyId(), annotation));
        }
        return changeList.stream();
    }
}
