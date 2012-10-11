package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import edu.stanford.bmir.protege.web.client.model.HasLexicalForm;
import org.semanticweb.owlapi.model.IRI;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2012
 * <p>
 *     Notes can either be replies to resources i.e. {@link IRI}s (corresponding to entity IRIs etc.) or replies to
 *     other replies i.e. {@link NoteId}s.
 *     This class captures this.
 * </p>
 */
public class InReplyToId implements HasLexicalForm, Serializable {

    private String iri;
    
    private NoteId noteId;

    /**
     * Empty constructor for the purposes of serialization
     */
    private InReplyToId() {

    }

    private InReplyToId(String iri) {
        this.iri = iri;
        this.noteId = null;
    }

    private InReplyToId(NoteId noteId) {
        this.noteId = noteId;
        this.iri = null;
    }

    /**
     * Gets an {@link InReplyToId} for an IRI.
     * @param iriLexicalForm The lexical form of the IRI.  Note <code>null</code>.
     * @return The {@link InReplyToId} for the IRI with the given lexical form.
     * @throws NullPointerException if the iriLexicalForm parameter is <code>null</code>.
     */
    public static InReplyToId createFromIRI(String iriLexicalForm) {
        if(iriLexicalForm == null) {
            throw new NullPointerException("iriLexicalForm must not be null");
        }
        return new InReplyToId(iriLexicalForm);
    }
    
    public static InReplyToId createFromNoteId(NoteId noteId) {
        return new InReplyToId(noteId);
    }
    
    public String getLexicalForm() {
        if(iri != null) {
            return iri;
        }
        else {
            return noteId.getLexicalForm();
        }
    }
    
    public boolean isIRI() {
        return iri != null;
    }
    
    public boolean isNoteId() {
        return noteId != null;
    }

    public String toIRI() {
        return iri;
    }

    public NoteId toNoteId() {
        return noteId;
    }
    
    
    @Override
    public int hashCode() {
        return InReplyToId.class.getSimpleName().hashCode() + (iri != null ? iri.hashCode() : 0) + (noteId != null ? noteId.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof InReplyToId)) {
            return false;
        }
        InReplyToId other = (InReplyToId) obj;
        return this.iri != null && other.iri != null && this.iri.equals(other.iri)
                || this.noteId != null && other.noteId != null && this.noteId.equals(other.noteId);
    }
    
}
