package edu.stanford.bmir.protege.web.shared.notes;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class NoteDeletedEvent extends ProjectEvent<NoteDeletedHandler> {

    public static final transient Event.Type<NoteDeletedHandler> TYPE = new Event.Type<NoteDeletedHandler>();

    private NoteId noteId;

    private NoteDeletedEvent() {
    }

    public NoteDeletedEvent(ProjectId source, NoteId noteId) {
        super(checkNotNull(source));
        this.noteId = checkNotNull(noteId);
    }

    public NoteId getNoteId() {
        return noteId;
    }

    @Override
    public Event.Type<NoteDeletedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NoteDeletedHandler handler) {
        handler.handleNoteDeleted(this);
    }
}
