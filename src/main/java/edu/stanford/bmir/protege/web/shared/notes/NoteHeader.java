package edu.stanford.bmir.protege.web.shared.notes;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 *
 */
@Deprecated
@Immutable
public final class NoteHeader implements Serializable, Comparable<NoteHeader> {

    private NoteId noteId;

    private Optional<NoteId> replyToId;

    private UserId author;

    private long timestamp;

    /**
     * For serialization only
     */
    private NoteHeader() {
    }


    public NoteHeader(NoteId noteId, Optional<NoteId> replyToId, UserId author, long timestamp) {
        this.noteId = noteId;
        this.replyToId = replyToId;
        this.author = author;
        this.timestamp = timestamp;
    }

    public NoteId getNoteId() {
        return noteId;
    }

    public Optional<NoteId> getReplyToId() {
        return replyToId;
    }

    public UserId getAuthor() {
        return author;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int hashCode() {
        return "NoteHeader".hashCode() + noteId.hashCode() + replyToId.hashCode() + (int) timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NoteHeader)) {
            return false;
        }
        NoteHeader other = (NoteHeader) obj;
        return this.noteId.equals(other.noteId) && this.author.equals(other.author) && this.timestamp == other.timestamp && this.replyToId.equals(other.replyToId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NoteHeader");
        sb.append("(");
        sb.append(noteId);
        sb.append(" ");
        sb.append(author);
        sb.append(" ");
        sb.append("TimeStamp(");
        sb.append(timestamp);
        sb.append(")");
        sb.append(")");
        return sb.toString();
    }

    @Override
    public int compareTo(NoteHeader o) {
        if(this.timestamp < o.timestamp) {
            return -1;
        }
        else if(this.timestamp > o.timestamp) {
            return 1;
        }
        int authorDelta = this.author.compareTo(o.author);
        if(authorDelta != 0) {
            return authorDelta;
        }
        int noteIdDelta = this.noteId.getLexicalForm().compareTo(o.noteId.getLexicalForm());
        if(noteIdDelta != 0) {
            return noteIdDelta;
        }
        return this.replyToId.toString().compareTo(o.replyToId.toString());
    }
}
