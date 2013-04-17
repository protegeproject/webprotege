package edu.stanford.bmir.protege.web.shared.notes;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 * <p>
 *     A Note is a small object which has a {@link NoteHeader} and also {@link NoteContent}.  The header identifies the
 *     note and records important information such as the time stamp (representing the time the note was posted), author
 *     etc.  The note content holds information specific to the note (which may be optional) such as a subject, body
 *     etc.
 * </p>
 */
@Immutable
public final class Note implements Serializable {

    private NoteHeader header;

    private NoteContent content;


    /**
     * Constructs a {@link Note}.
     * @param header The header of the note.  Not {@code null}.
     * @param content The content of the note.  Note {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    private Note(NoteHeader header, NoteContent content) {
        this.header = header;
        this.header = checkNotNull(header);
        this.content = checkNotNull(content);
    }

    /**
     * Default constructor for serialization purposes only.
     */
    private Note() {
    }

    public static Note createNote(NoteHeader noteHeader, NoteContent noteContent) {
        return new Note(noteHeader, noteContent);
    }

    public static Note createNote(NoteId noteId, Optional<NoteId> inReplyTo, long timestamp, UserId author, NoteType noteType, Optional<String> subject, String body) {
        final NoteHeader header = new NoteHeader(noteId, inReplyTo, author, timestamp);
        NoteContent content = NoteContent.builder().setBody(body).setSubject(subject).setNoteType(noteType).build();
        return new Note(header, content);
    }
    
    public static Note createComment(NoteId noteId, Optional<NoteId> inReplyTo, long createdAt, UserId createdBy, Optional<String> subject, String body) {
        return createNote(noteId, inReplyTo, createdAt, createdBy, NoteType.COMMENT, subject, body);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////
    //////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets the identifier for this Note.
     * @return The NoteId for this Note.  Not <code>null</code>.
     */
    public NoteId getNoteId() {
        return header.getNoteId();
    }

    /**
     * Get the {@link Optional} {@link NoteId} that this {@link Note} is a reply to.
     * @return The {@link Optional} {@link NoteId} representing the {@link Note} that this {@link Note} is a reply to.
     */
    public Optional<NoteId> getInReplyTo() {
        return header.getReplyToId();
    }

    public long getTimestamp() {
        return header.getTimestamp();
    }

    public UserId getAuthor() {
        return header.getAuthor();
    }

    public String getSubject() {
        return content.getSubject().or("");
    }


    public String getBody() {
        return content.getBody().or("");
    }


    public NoteHeader getHeader() {
        return header;
    }

    public NoteContent getContent() {
        return content;
    }

    @Override
    public int hashCode() {
        return "Note".hashCode() + header.hashCode() + content.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Note)) {
            return false;
        }
        Note other = (Note) obj;
        return this.header.equals(other.header) && this.content.equals(other.content);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////   A Builder for Notes
    //////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public static class Builder {

        private NoteId noteId;

        private Optional<NoteId> replyToId;

        private long timestamp;

        private NoteType noteType;

        private UserId author;

        private Optional<String> subject;

        private String body;

        /**
         * Creates a {@link Builder} for {@link Note}s using the specified note header.
         * @param noteId The {@link NoteId}.  Not {@code null}.
         * @throws NullPointerException if any parameters are {@code null}.
         */
        public Builder(NoteId noteId, UserId author) {
            this.noteId = noteId;
            this.author = author;
            this.replyToId = Optional.absent();
            this.timestamp = new Date().getTime();
            this.noteType = NoteType.COMMENT;
            this.subject = Optional.absent();
            this.body = "";
        }

        /**
         * Sets the {@link NoteId} that this note will reply to.
         * @param inReplyTo The id of the note that this note will reply to.  Not {@code null}.
         * @throws NullPointerException if {@code inReplyTo} is {@code null}.
         */
        public void setInReplyTo(Optional<NoteId> inReplyTo) {
            this.replyToId = checkNotNull(inReplyTo);
        }

        /**
         * Sets the {@link NoteType}.  By default this is a comment.
         * @param noteType The {@link NoteType}. Not {@code null}.
         * @throws  NullPointerException if {@code noteType} is {@code null}.
         */
        public void setNoteType(NoteType noteType) {
            this.noteType = checkNotNull(noteType);
        }

        /**
         * Sets the subject.
         * @param subject A string representing the subject.  May be empty.  Not {@code null}.
         * @throws NullPointerException if {@code subject} is {@code null}.
         */
        public void setSubject(Optional<String> subject) {
            this.subject = checkNotNull(subject);
        }

        /**
         * Sets the note body.
         * @param body A string representing the body of the note.  May be empty.  Not {@code null}.
         * @throws NullPointerException if {@code body} is {@code null}.
         */
        public void setBody(String body) {
            this.body = checkNotNull(body);
        }


        /**
         * Builds a {@link Note} from the information in this builder.
         * @return The built {@link Note} object.
         */
        public Note build() {
            NoteHeader noteHeader = new NoteHeader(noteId, replyToId, author, timestamp);
            NoteContent noteContent = NoteContent.builder().setBody(body).setSubject(subject).setNoteType(noteType).build();
            return new Note(noteHeader, noteContent);
        }
    }

}
