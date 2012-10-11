package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/08/2012
 * <p>
 *     Represents a the content of a simple reply which has a type, a subject and a body.
 *     Note that this class DOES NOT capture the relationship between replies.
 * </p>
 * <p>
 *     ReplyContent objects are immutable.
 * </p>
 */
public final class NoteContent implements Serializable, Comparable<NoteContent> {

    private NoteType noteType;
    
    private String subject;
    
    private String body;


    /**
     * Empty constructor for the purposes of serialization
     */
    private NoteContent() {
    }

    /**
     * Constructs a NoteContent.
     *
     * @param noteType The IRI that identifies the type of the note. Not null.
     * @param subject The subject of the note.  Not null.
     * @param body The body of the note.  Not null.
     * @throws NullPointerException if any parameters are null.
     */
    public NoteContent(NoteType noteType, String subject, String body) {
        if(noteType == null) {
            throw new NullPointerException("noteType must not be null");
        }
        if(subject == null) {
            throw new NullPointerException("subject must not be null");
        }
        if(body == null) {
            throw new NullPointerException("body must not be null");
        }
        this.noteType = noteType;
        this.subject = subject;
        this.body = body;
    }

    public NoteContent createEmptyComment() {
        return createComment("", "");
    }

    /**
     * Creates a Note which has a type equal to Comment (see {@link NoteType#COMMENT}
     *
     * @param subject The subject of the note.  Not null.
     * @param body The body of the note.  Not null.
     * @throws NullPointerException if any parameters are null.
     * @return The new Note which will have a type of comment.
     */
    public static NoteContent createComment(String subject, String body) {
        return new NoteContent(NoteType.getComment(), subject, body);
    }


    /**
     * Gets the note type.  See the {@link NoteType} enum for a list of common types.  The precise relationship between
     * note types is defined elsewhere (by an ontology!).
     * @return The IRI that identifies the note type.  Not null.
     */
    public NoteType getNoteType() {
        return noteType;
    }

    /**
     * Gets the subject.
     * @return A string representing the subject of the note.  Not null.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Gets the body/content of the note.
     * @return A string representing the body of the note.  Not null.
     */
    public String getBody() {
        return body;
    }

    @Override
    public int hashCode() {
        return NoteContent.class.getSimpleName().hashCode() + noteType.hashCode()
                + subject.hashCode()
                + body.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NoteContent)) {
            return false;
        }
        NoteContent other = (NoteContent) obj;
        return this.subject.equals(other.subject)
                && this.body.equals(other.body);
    }

    /**
     * Compares this note with another note. Notes comparisons are based on timestamp followed by subject,
     * author name, body and note type.  If all of these fields compare equal then the notes are the same.
     * @param o The other note for comparison.
     * @return The relative position of this note in comparison with the specified note.
     */
    public int compareTo(NoteContent o) {
        // Subject
        int subjectDiff = this.subject.compareTo(o.subject);
        if(subjectDiff != 0) {
            return subjectDiff;
        }
        // Body
        int bodyDiff = this.body.compareTo(o.body);
        if(bodyDiff != 0) {
            return bodyDiff;
        }
        // Note type
        int noteTypeDiff = this.noteType.compareTo(o.noteType);
        if(noteTypeDiff != 0) {
            return noteTypeDiff;
        }
        // Same
        return 0;
    }

    public boolean isEmpty() {
        return subject.isEmpty() && body.isEmpty();
    }

    public boolean isSubjectEmpty() {
        return subject.isEmpty();
    }

    public boolean isBodyEmpty() {
        return body.isEmpty();
    }
}
