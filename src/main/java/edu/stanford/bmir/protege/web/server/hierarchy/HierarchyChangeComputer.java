package edu.stanford.bmir.protege.web.server.hierarchy;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import edu.stanford.bmir.protege.web.server.events.EventTranslator;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyRootAddedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyRootRemovedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public abstract class HierarchyChangeComputer<T extends OWLEntity> implements EventTranslator {

    private final ProjectId projectId;

    private final OWLObjectHierarchyProvider<T> hierarchyProvider;

    private final EntityType<T> entityType;

    private final HierarchyId<T> hierarchyId;


    private SetMultimap<T, T> child2ParentMap = HashMultimap.create();

    private Set<T> roots = new HashSet<T>();

    public HierarchyChangeComputer(ProjectId projectId, EntityType<T> entityType, OWLObjectHierarchyProvider<T> hierarchyProvider, HierarchyId<T> hierarchyId) {
        this.projectId = projectId;
        this.hierarchyProvider = hierarchyProvider;
        this.entityType = entityType;
        this.hierarchyId = hierarchyId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void prepareForOntologyChanges(List<OWLOntologyChange> submittedChanges) {
        for(OWLOntologyChange change : submittedChanges) {
            for(OWLEntity entity : change.getSignature()) {
                if(entity.isType(entityType)) {
                    final T t = (T) entity;
                    final Set<T> parentsBefore = hierarchyProvider.getParents(t);
                    child2ParentMap.putAll(t, parentsBefore);
                }
            }
        }
        roots.addAll(hierarchyProvider.getRoots());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void translateOntologyChanges(Revision revision, List<OWLOntologyChange> appliedChanges, List<ProjectEvent<?>> projectEventList) {
        Set<T> changeSignature = new HashSet<T>();
        for(OWLOntologyChange change : appliedChanges) {
            for(OWLEntity child : change.getSignature()) {
                if(child.isType(entityType)) {
                    final T t = (T) child;
                    if (!changeSignature.contains(t)) {
                        changeSignature.add(t);
                        Set<T> parentsBefore = child2ParentMap.get(t);
                        Set<T> parentsAfter = hierarchyProvider.getParents(t);
                        for(T parentBefore : parentsBefore) {
                            if(!parentsAfter.contains(parentBefore)) {
                                // Removed
                                projectEventList.add(createRemovedEvent(t, parentBefore));
                            }
                        }
                        for(T parentAfter : parentsAfter) {
                            if(!parentsBefore.contains(parentAfter)) {
                                // Added
                                projectEventList.add(createAddedEvent(t, parentAfter));
                            }
                        }
                    }
                }
            }
        }
        Set<T> rootsAfter = new HashSet<T>(hierarchyProvider.getRoots());
        for(T rootAfter : rootsAfter) {
            if(!roots.contains(rootAfter)) {
                projectEventList.add(new HierarchyRootAddedEvent<T>(projectId, hierarchyId, rootAfter));
            }
        }
        for(T rootBefore : roots) {
            if(!rootsAfter.contains(rootBefore)) {
                projectEventList.add(new HierarchyRootRemovedEvent<T>(projectId, hierarchyId, rootBefore));
            }
        }
    }


    protected abstract HierarchyChangedEvent<T, ?> createRemovedEvent(T child, T parent);

    protected abstract HierarchyChangedEvent<T, ?> createAddedEvent(T child, T parent);

}
