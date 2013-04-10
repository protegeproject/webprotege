package edu.stanford.bmir.protege.web.shared.notes;

import com.google.common.base.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2013
 */
public class NoteRemovedEvent {

    private NoteId noteId;

    private Optional<NoteId> inReplyTo;

    public NoteRemovedEvent(NoteId noteId, Optional<NoteId> inReplyTo) {
        this.noteId = noteId;
        this.inReplyTo = inReplyTo;
    }

    public NoteId getNoteId() {
        return noteId;
    }

    public Optional<NoteId> getInReplyTo() {
        return inReplyTo;
    }
}
