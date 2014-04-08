package edu.stanford.bmir.protege.web.server.notes.api;

import edu.stanford.bmir.protege.web.shared.notes.NoteField;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class SetFieldValue<T extends Serializable> extends NoteChange {

    private NoteId noteId;

    private NoteField<T> noteField;

    private T value;

    public SetFieldValue(NoteId noteId, NoteField<T> noteField, T value) {
        this.noteId = noteId;
        this.noteField = noteField;
        this.value = value;
    }

    public NoteId getNoteId() {
        return noteId;
    }

    public NoteField<T> getNoteField() {
        return noteField;
    }

    public T getValue() {
        return value;
    }
}
