package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class HierarchyRootAddedEvent<T extends Serializable> extends ProjectEvent<HierarchyRootAddedHandler<T>> {

    public transient static final Type<HierarchyRootAddedHandler<?>> TYPE = new Type<HierarchyRootAddedHandler<?>>();

    private HierarchyId<T> hierarchyId;

    private T root;

    public HierarchyRootAddedEvent(ProjectId source, HierarchyId<T> hierarchyId, T root) {
        super(source);
        this.hierarchyId = hierarchyId;
        this.root = root;
    }

    @SuppressWarnings("unused")
    private HierarchyRootAddedEvent() {
    }

    public HierarchyId<T> getHierarchyId() {
        return hierarchyId;
    }

    public T getRoot() {
        return root;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> Type<HierarchyRootAddedEvent<T>> getType() {
        return (Type) TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Type<HierarchyRootAddedHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(HierarchyRootAddedHandler<T> handler) {
        handler.handleHierarchyRootAdded(this);
    }
}
