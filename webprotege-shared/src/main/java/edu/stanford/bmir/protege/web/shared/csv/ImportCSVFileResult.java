package edu.stanford.bmir.protege.web.shared.csv;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/05/2013
 */
public class ImportCSVFileResult implements Result, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> eventList;

    private int rowCount;

    /**
     * For serialization purposes only
     */
    private ImportCSVFileResult() {
    }

    public ImportCSVFileResult(EventList<ProjectEvent<?>> eventList, int rowCount) {
        this.eventList = eventList;
        this.rowCount = rowCount;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }

    public int getRowCount() {
        return rowCount;
    }
}
