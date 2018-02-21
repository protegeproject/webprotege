package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.events.ProjectEventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class GetProjectEventsResult implements Result {

    private ProjectEventList events;

    /**
     * For serialization purposes only
     */
    private GetProjectEventsResult() {
    }

    public GetProjectEventsResult(ProjectEventList events) {
        this.events = events;
    }

    public ProjectEventList getEvents() {
        return events;
    }
}
