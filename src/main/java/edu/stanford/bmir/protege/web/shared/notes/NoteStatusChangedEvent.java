package edu.stanford.bmir.protege.web.shared.notes;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/04/2013
 */
public class NoteStatusChangedEvent extends ProjectEvent<NoteStatusChangedHandler> {

    public static final transient Event.Type<NoteStatusChangedHandler> TYPE = new Event.Type<NoteStatusChangedHandler>();

    private NoteId noteId;

    private NoteStatus noteStatus;

    private NoteStatusChangedEvent() {
    }

    public NoteStatusChangedEvent(ProjectId source, NoteId noteId, NoteStatus noteStatus) {
        super(source);
        this.noteId = noteId;
        this.noteStatus = noteStatus;
    }

    @Override
    public Event.Type<NoteStatusChangedHandler> getAssociatedType() {
        return TYPE;
    }

    public NoteId getNoteId() {
        return noteId;
    }

    public NoteStatus getNoteStatus() {
        return noteStatus;
    }

    @Override
    protected void dispatch(NoteStatusChangedHandler handler) {
        handler.handleNoteStatusChanged(this);
    }
}
