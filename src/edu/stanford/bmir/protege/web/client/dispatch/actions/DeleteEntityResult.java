package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class DeleteEntityResult implements Result, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> events;

    /**
     * For serialization only
     */
    private DeleteEntityResult() {
    }


    public DeleteEntityResult(EventList<ProjectEvent<?>> events) {
        this.events = events;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return events;
    }
}
