package edu.stanford.bmir.protege.web.server.notes.api;

import edu.stanford.bmir.protege.web.shared.notes.Note;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class AddNote extends NoteChange {

    private Note note;

    public AddNote(Note note) {
        this.note = note;
    }

    private AddNote() {
    }

    @Override
    public int hashCode() {
        return "AddNote".hashCode() + note.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AddNote)) {
            return false;
        }
        AddNote other = (AddNote) obj;
        return this.note.equals(other.note);
    }
}
