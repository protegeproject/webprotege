package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public class UnknownNoteIdException extends DiscussionThreadManagerException {

    private NoteId noteId;

    public UnknownNoteIdException(NoteId noteId) {
        super("Unkown NoteId: " + noteId);
        this.noteId = noteId;
    }

    public NoteId getNoteId() {
        return noteId;
    }
}
