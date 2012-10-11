package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import edu.stanford.bmir.protege.web.client.model.HasLexicalForm;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/08/2012
 */
public class NoteFieldId implements HasLexicalForm, Serializable {

    private String iri;
    
    private String shortName;

    private NoteFieldId() {
        // Empty for serialization purposes
    }
    
    private NoteFieldId(String iri, String shortName) {
        if(iri == null) {
            throw new NullPointerException("IRI must not be null");
        }
        if(shortName == null) {
            throw new NullPointerException("Short name must not be null");
        }
        this.iri = iri;
        this.shortName = shortName;
    }
    
    
    
    public static NoteFieldId getNoteFieldId(String iri, String shortName) {
        return new NoteFieldId(iri, shortName);
    }

    public static NoteFieldId getSubjectFieldId() {
        return new NoteFieldId("subject", "Subject");
    }

    public static NoteFieldId getBodyFieldId() {
        return new NoteFieldId("body", "Body");
    }

    public static NoteFieldId getCreatedAtFieldId() {
        return new NoteFieldId("createdAt", "Created At");
    }

    public static NoteFieldId getCreatedByFieldId() {
        return new NoteFieldId("createdBy", "Created By");
    }

    public String getLexicalForm() {
        return iri;
    }

    public String getShortName() {
        return shortName;
    }
    
    @Override
    public int hashCode() {
        return NoteFieldId.class.getSimpleName().hashCode() + iri.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NoteFieldId)) {
            return false;
        }
        NoteFieldId other = (NoteFieldId) obj;
        return this.iri.equals(other.iri);
    }
}
