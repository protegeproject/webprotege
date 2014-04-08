package edu.stanford.bmir.protege.web.server.notes.api;

import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class ReplaceNoteContent extends NoteChange {

    private NoteId noteId;

    private NoteContent newContent;

    public ReplaceNoteContent(NoteId noteId, NoteContent newContent) {
        this.noteId = noteId;
        this.newContent = newContent;
    }

    private ReplaceNoteContent() {
    }

    public NoteContent getNewContent() {
        return newContent;
    }

    @Override
    public int hashCode() {
        return "ReplaceNoteContent".hashCode() + noteId.hashCode() + newContent.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ReplaceNoteContent)) {
            return false;
        }
        ReplaceNoteContent other = (ReplaceNoteContent) obj;
        return this.noteId.equals(other.noteId) && this.getNewContent().equals(other.getNewContent());
    }
}
