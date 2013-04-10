package edu.stanford.bmir.protege.web.shared.watches;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class AddWatchResult implements Result, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> events;

    private AddWatchResult() {
    }

    public AddWatchResult(EventList<ProjectEvent<?>> events) {
        this.events = events;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return events;
    }
}
