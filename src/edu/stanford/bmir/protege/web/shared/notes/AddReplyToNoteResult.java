package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public class AddReplyToNoteResult implements Result, HasEventList<ProjectEvent<?>> {

    private Note note;

    private EventList<ProjectEvent<?>> eventList;

    private AddReplyToNoteResult() {
    }

    public AddReplyToNoteResult(Note note, EventList<ProjectEvent<?>> eventList) {
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
