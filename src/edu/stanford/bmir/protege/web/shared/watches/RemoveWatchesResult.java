package edu.stanford.bmir.protege.web.shared.watches;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasEventListResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class RemoveWatchesResult extends AbstractHasEventListResult<ProjectEvent<?>> {

    public RemoveWatchesResult(EventList<ProjectEvent<?>> eventList) {
        super(eventList);
    }

    private RemoveWatchesResult() {
    }
}
