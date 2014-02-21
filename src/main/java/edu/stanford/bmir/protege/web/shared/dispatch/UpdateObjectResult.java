package edu.stanford.bmir.protege.web.shared.dispatch;

import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UpdateObjectResult implements Result, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> events;

    private UpdateObjectResult() {
    }

    public UpdateObjectResult(EventList<ProjectEvent<?>> events) {
        this.events = events;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return events;
    }

}
