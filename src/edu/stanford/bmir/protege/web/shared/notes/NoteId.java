package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.shared.HasLexicalForm;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/08/2012
 * <p>
 *     Represents an identifier for a {@link edu.stanford.bmir.protege.web.shared.notes.Note}.  The identifier has a lexical form which uniquely determines it.
 * </p>
 * <p>
 *     NoteId objects are immutable.
 * </p>
 */
public final class NoteId implements HasLexicalForm, Serializable {

    private String lexicalForm;

    // Empty constructor for the purposes of serialization
    private NoteId() {

    }


    private NoteId(String lexicalForm) {
        this.lexicalForm = lexicalForm;
    }

    /**
     * Gets the lexical form of this ReplyId.  This can be used to persist an id.
     * @return A string that represents the lexical form of this ReplyId.
     */
    public String getLexicalForm() {
        return lexicalForm;
    }

    /**
     * Creates a NoteId from a lexical form.
     * @param replyIdLexicalForm The lexical form from which to create the ReplyId.
     * @return A ReplyId that has the specified lexical form.  Not null.
     * @throws NullPointerException if replyIdLexicalForm is null.
     */
    public static NoteId createNoteIdFromLexicalForm(String replyIdLexicalForm) {
        final String id = checkNotNull(replyIdLexicalForm, "replyIdLexicalForm must not be null");
        return new NoteId(id);
    }



    @Override
    public int hashCode() {
        return "NoteId".hashCode() + lexicalForm.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NoteId)) {
            return false;
        }
        NoteId other = (NoteId) obj;
        return this.lexicalForm.equals(other.lexicalForm);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NoteId");
        sb.append("(");
        sb.append(lexicalForm);
        sb.append(")");
        return sb.toString();
    }

}
