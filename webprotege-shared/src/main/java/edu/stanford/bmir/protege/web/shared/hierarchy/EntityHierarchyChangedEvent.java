package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphModelChangedEvent;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 1 Dec 2017
 */
public class EntityHierarchyChangedEvent extends ProjectEvent<EntityHierarchyChangedHandler> {

    public static final transient Event.Type<EntityHierarchyChangedHandler> ON_HIERARCHY_CHANGED = new Event.Type<>();

    private HierarchyId hierarchyId;

    private GraphModelChangedEvent<EntityNode> changeEvent;

    public EntityHierarchyChangedEvent(@Nonnull ProjectId source,
                                       @Nonnull HierarchyId hierarchyId,
                                       @Nonnull GraphModelChangedEvent<EntityNode> changeEvent) {
        super(source);
        this.hierarchyId = checkNotNull(hierarchyId);
        this.changeEvent = checkNotNull(changeEvent);
    }

    @Nonnull
    public HierarchyId getHierarchyId() {
        return hierarchyId;
    }

    @GwtSerializationConstructor
    private EntityHierarchyChangedEvent() {
    }

    @Override
    public Event.Type<EntityHierarchyChangedHandler> getAssociatedType() {
        return ON_HIERARCHY_CHANGED;
    }

    @Override
    protected void dispatch(EntityHierarchyChangedHandler handler) {
        handler.handleHierarchyChanged(this);
    }

    public GraphModelChangedEvent<EntityNode> getChangeEvent() {
        return changeEvent;
    }
}
