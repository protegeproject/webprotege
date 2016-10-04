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
public class DeleteNoteResult extends AbstractHasEventListResult<ProjectEvent<?>> {

    private NoteId noteId;

    private DeleteNoteResult() {
    }

    public DeleteNoteResult(NoteId noteId, EventList<ProjectEvent<?>> events) {
        super(events);
        this.noteId = noteId;
    }

    public NoteId getNoteId() {
        return noteId;
    }


}
