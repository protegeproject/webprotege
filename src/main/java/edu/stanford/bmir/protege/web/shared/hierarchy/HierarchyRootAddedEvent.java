package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class HierarchyRootAddedEvent<T extends OWLEntity> extends ProjectEvent<HierarchyRootAddedHandler<T>> {

    public transient static final Event.Type<HierarchyRootAddedHandler<?>> TYPE = new Event.Type<HierarchyRootAddedHandler<?>>();

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
    public static <T extends OWLEntity> Event.Type<HierarchyRootAddedEvent<T>> getType() {
        return (Event.Type) TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Event.Type<HierarchyRootAddedHandler<T>> getAssociatedType() {
        return (Event.Type) TYPE;
    }

    @Override
    protected void dispatch(HierarchyRootAddedHandler<T> handler) {
        handler.handleHierarchyRootAdded(this);
    }
}
