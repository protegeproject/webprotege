package edu.stanford.bmir.protege.web.server.watches;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.hierarchy.*;
import edu.stanford.bmir.protege.web.server.index.ProjectClassAssertionAxiomsByIndividualIndex;
import edu.stanford.bmir.protege.web.server.revision.EntitiesByRevisionCache;
import edu.stanford.bmir.protege.web.server.revision.ProjectChangesManager;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.watches.WatchType.BRANCH;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/05/15
 */
@ProjectSingleton
public class WatchedChangesManager {

    private final ClassHierarchyProvider classHierarchyProvider;

    private final ObjectPropertyHierarchyProvider objectPropertyHierarchyProvider;

    private final DataPropertyHierarchyProvider dataPropertyHierarchyProvider;

    private final AnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider;

    private final RevisionManager changeManager;

    private final ProjectChangesManager projectChangesManager;

    private final EntitiesByRevisionCache entitiesByRevisionCache;

    private final ProjectClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex;

    @Inject
    public WatchedChangesManager(ProjectChangesManager projectChangesManager,
                                 ClassHierarchyProvider classHierarchyProvider,
                                 ObjectPropertyHierarchyProvider objectPropertyHierarchyProvider,
                                 DataPropertyHierarchyProvider dataPropertyHierarchyProvider,
                                 AnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider,
                                 RevisionManager changeManager,
                                 EntitiesByRevisionCache entitiesByRevisionCache,
                                 ProjectClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex) {
        this.projectChangesManager = checkNotNull(projectChangesManager);
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
        this.objectPropertyHierarchyProvider = checkNotNull(objectPropertyHierarchyProvider);
        this.dataPropertyHierarchyProvider = checkNotNull(dataPropertyHierarchyProvider);
        this.annotationPropertyHierarchyProvider = checkNotNull(annotationPropertyHierarchyProvider);
        this.classAssertionAxiomsByIndividualIndex = classAssertionAxiomsByIndividualIndex;
        this.changeManager = checkNotNull(changeManager);
        this.entitiesByRevisionCache = checkNotNull(entitiesByRevisionCache);
    }

    public ImmutableList<ProjectChange> getProjectChangesForWatches(Set<Watch> watches) {
        Set<OWLEntity> superEntities = new HashSet<>();
        Set<OWLEntity> directWatches = new HashSet<>();
        for (Watch watch : watches) {
            if (watch.getType() == BRANCH) {
                OWLEntity entity = watch.getEntity();
                superEntities.add(entity);
                directWatches.add(entity);
            }
            else {
                directWatches.add(watch.getEntity());
            }
        }
        if (superEntities.isEmpty() && directWatches.isEmpty()) {
            return ImmutableList.of();
        }
        ImmutableList.Builder<ProjectChange> result = ImmutableList.builder();
        List<Revision> revisionsCopy = changeManager.getRevisions();
        for (Revision revision : revisionsCopy) {
            for (OWLEntity watchedEntity : getWatchedEntities(superEntities, directWatches, revision)) {
                ImmutableList<ProjectChange> changes = projectChangesManager.getProjectChangesForSubjectInRevision(watchedEntity, revision);
                result.addAll(changes);
            }
        }
        return result.build();
    }

    private Set<OWLEntity> getWatchedEntities(Set<OWLEntity> superEntities, Set<OWLEntity> directWatches, Revision revision) {
        Set<OWLEntity> watchedEntities = new HashSet<>();
        Set<OWLEntity> entities = entitiesByRevisionCache.getEntities(revision);
        for (OWLEntity entity : entities) {
            if (directWatches.contains(entity)) {
                watchedEntities.add(entity);
            }
            else {
                boolean watchedByAncestor = isWatchedByAncestor(superEntities, entity);
                if (watchedByAncestor) {
                    watchedEntities.add(entity);
                }
            }
        }
        return watchedEntities;
    }


    private Boolean isWatchedByAncestor(final Set<OWLEntity> watchedAncestors, OWLEntity entity) {
        return entity.accept(new OWLEntityVisitorEx<>() {
            @Nonnull
            @Override
            public Boolean visit(@Nonnull OWLClass cls) {
                final var ancestors = classHierarchyProvider.getAncestors(cls);
                return isWatchedByAncestor(ancestors);
            }


            @Nonnull
            @Override
            public Boolean visit(@Nonnull OWLObjectProperty property) {
                return isWatchedByAncestor(objectPropertyHierarchyProvider.getAncestors(property));
            }

            @Nonnull
            @Override
            public Boolean visit(@Nonnull OWLDataProperty property) {
                return isWatchedByAncestor(dataPropertyHierarchyProvider.getAncestors(property));
            }

            @Nonnull
            @Override
            public Boolean visit(@Nonnull OWLNamedIndividual individual) {
                return classAssertionAxiomsByIndividualIndex.getClassAssertionAxioms(individual)
                        .map(OWLClassAssertionAxiom::getClassExpression)
                        .filter(OWLClassExpression::isNamed)
                        .map(OWLClassExpression::asOWLClass)
                        .anyMatch(this::isWatchedByAncestor);
            }

            private Boolean isWatchedByAncestor(OWLClass cls) {
                return isWatchedByAncestor(classHierarchyProvider.getAncestors(cls));
            }

            @Nonnull
            @Override
            public Boolean visit(@Nonnull OWLDatatype datatype) {
                return false;
            }

            @Nonnull
            @Override
            public Boolean visit(@Nonnull OWLAnnotationProperty property) {
                return isWatchedByAncestor(annotationPropertyHierarchyProvider.getAncestors(property));
            }

            private Boolean isWatchedByAncestor(Collection<? extends OWLEntity> ancestors) {
                for (OWLEntity anc : ancestors) {
                    if (watchedAncestors.contains(anc)) {
                        return true;
                    }
                }
                return false;
            }

        });
    }

}
