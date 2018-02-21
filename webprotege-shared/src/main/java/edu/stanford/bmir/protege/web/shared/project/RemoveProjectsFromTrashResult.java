package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasEventListResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedFromTrashEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class RemoveProjectsFromTrashResult extends AbstractHasEventListResult<ProjectMovedFromTrashEvent> {

    private RemoveProjectsFromTrashResult() {
    }

    public RemoveProjectsFromTrashResult(EventList<ProjectMovedFromTrashEvent> eventList) {
        super(eventList);
    }
}
