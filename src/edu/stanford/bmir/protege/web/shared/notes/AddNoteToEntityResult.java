package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class AddNoteToEntityResult implements Result, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> eventList;

    private Note note;

    /**
     * For serialization only!
     */
    private AddNoteToEntityResult() {
    }

    public AddNoteToEntityResult(EventList<ProjectEvent<?>> eventList, Note note) {
        this.eventList = eventList;
        this.note = note;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }

    public Note getNote() {
        return note;
    }
}
