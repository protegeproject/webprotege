package edu.stanford.bmir.protege.web.shared.notes;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2013
 */
public class NoteEditedEvent {

    private NoteId noteId;

    public NoteEditedEvent(NoteId noteId) {
        this.noteId = noteId;
    }

    public NoteId getNoteId() {
        return noteId;
    }
}
