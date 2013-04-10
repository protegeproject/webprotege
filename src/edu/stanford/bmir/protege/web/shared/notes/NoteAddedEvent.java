package edu.stanford.bmir.protege.web.shared.notes;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.Event;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2013
 */
public class NoteAddedEvent {

    private NoteId noteId;

    private Optional<NoteId> inReplyTo;

    public NoteAddedEvent(NoteId noteId, Optional<NoteId> inReplyTo) {
        this.noteId = checkNotNull(noteId);
        this.inReplyTo = checkNotNull(inReplyTo);
    }

    public NoteId getNoteId() {
        return noteId;
    }

    public Optional<NoteId> getInReplyTo() {
        return inReplyTo;
    }
}
