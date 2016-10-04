package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasEventListResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public class AddReplyToNoteResult extends AbstractHasEventListResult<ProjectEvent<?>> {

    private Note note;

    private AddReplyToNoteResult() {
    }

    public AddReplyToNoteResult(Note note, EventList<ProjectEvent<?>> eventList) {
        super(eventList);
        this.note = note;
    }


    public Note getNote() {
        return note;
    }
}
