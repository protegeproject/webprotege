package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
public class MoveHierarchyNodeResult implements Result, HasEventList<ProjectEvent<?>> {

    private boolean moved;

    private EventList<ProjectEvent<?>> eventList;

    public MoveHierarchyNodeResult(boolean moved, EventList<ProjectEvent<?>> eventList) {
        this.moved = moved;
        this.eventList = eventList;
    }

    @GwtSerializationConstructor
    private MoveHierarchyNodeResult() {
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }

    public boolean isMoved() {
        return moved;
    }
}
