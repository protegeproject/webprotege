package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasEventListResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedToTrashEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class MoveProjectsToTrashResult extends AbstractHasEventListResult<ProjectMovedToTrashEvent> {

    private MoveProjectsToTrashResult() {
    }

    public MoveProjectsToTrashResult(EventList<ProjectMovedToTrashEvent> eventList) {
        super(eventList);
    }
}
