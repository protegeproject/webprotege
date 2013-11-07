package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasEventListResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class AddNoteToEntityResult extends AbstractHasEventListResult<ProjectEvent<?>> {

    private Note note;

    /**
     * For serialization only!
     */
    private AddNoteToEntityResult() {
    }

    public AddNoteToEntityResult(EventList<ProjectEvent<?>> eventList, Note note) {
        super(eventList);
        this.note = note;
    }

    public Note getNote() {
        return note;
    }
}
