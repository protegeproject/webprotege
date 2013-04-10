package edu.stanford.bmir.protege.web.server.notes.api;

import edu.stanford.bmir.protege.web.shared.notes.NoteId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class RemoveNote extends NoteChange {

    private NoteId noteId;

    public RemoveNote(NoteId noteId) {
        this.noteId = noteId;
    }

    private RemoveNote() {
    }

    @Override
    public int hashCode() {
        return "RemoveNote".hashCode() + noteId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof RemoveNote)) {
            return false;
        }
        RemoveNote other = (RemoveNote) obj;
        return this.noteId.equals(other.noteId);
    }
}
