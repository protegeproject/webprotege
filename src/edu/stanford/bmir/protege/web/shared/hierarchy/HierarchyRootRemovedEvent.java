package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class HierarchyRootRemovedEvent<T> extends ProjectEvent<HierarchyRootRemovedHandler<T>> {

    public transient static final Type<HierarchyRootRemovedHandler<?>> TYPE = new Type<HierarchyRootRemovedHandler<?>>();

    private HierarchyId<T> hierarchyId;

    private T root;

    public HierarchyRootRemovedEvent(ProjectId source, HierarchyId<T> hierarchyId, T root) {
        super(source);
        this.hierarchyId = hierarchyId;
        this.root = root;
    }

    private HierarchyRootRemovedEvent() {
    }

    public HierarchyId<T> getHierarchyId() {
        return hierarchyId;
    }

    public T getRoot() {
        return root;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Type<HierarchyRootRemovedHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(HierarchyRootRemovedHandler<T> handler) {
        handler.handleHierarchyRootRemoved(this);
    }
}
