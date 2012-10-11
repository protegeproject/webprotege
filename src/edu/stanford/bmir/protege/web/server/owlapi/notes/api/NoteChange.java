package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2012
 */
public abstract class NoteChange {

    private NoteId noteId;

    protected NoteChange(NoteId noteId) {
        this.noteId = noteId;
    }
}
