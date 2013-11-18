package edu.stanford.bmir.protege.web.shared.notes;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class NoteDetails implements Serializable {

    private NoteHeader noteHeader;

    private NoteContent noteContent;

    /**
     * For serialization only
     */
    private NoteDetails() {
    }

    private NoteDetails(NoteHeader noteHeader) {
        this.noteHeader = noteHeader;
    }

    public NoteDetails(NoteHeader noteHeader, NoteContent noteContent) {
        this.noteHeader = noteHeader;
        this.noteContent = noteContent;
    }

    public NoteHeader getNoteHeader() {
        return noteHeader;
    }

    public NoteContent getNoteContent() {
        return noteContent;
    }

    @Override
    public int hashCode() {
        return "NoteDetails".hashCode() + noteHeader.hashCode() + noteContent.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NoteDetails)) {
            return false;
        }
        NoteDetails other = (NoteDetails) obj;
        return this.noteHeader.equals(other.noteHeader) && this.noteContent.equals(other.noteContent);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NoteDetails");
        sb.append("(");
        sb.append(noteHeader);
        sb.append(" ");
        sb.append(noteContent);
        sb.append(")");
        return sb.toString();
    }
}
