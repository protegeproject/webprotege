package edu.stanford.bmir.protege.web.shared.watches;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasEventListResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class AddWatchResult extends AbstractHasEventListResult<ProjectEvent<?>> {


    private AddWatchResult() {
    }

    public AddWatchResult(EventList<ProjectEvent<?>> events) {
        super(events);
    }

}
