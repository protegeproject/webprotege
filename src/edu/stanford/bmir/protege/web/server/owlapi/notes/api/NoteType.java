package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/08/2012
 */
public final class NoteType implements Serializable, Comparable<NoteType> {
   
    private static final NoteType COMMENT = getReplyTypeFromShortName("Comment");


    public static NoteType getNoteTypeFromIRI(String iriLexicalForm) {
        return new NoteType(iriLexicalForm, iriLexicalForm);
    }

    public static NoteType getReplyTypeFromShortName(String shortName) {
        String fullIRI = NotesVocabulary.NOTES_VOCABULARY_BASE + shortName;
        return new NoteType(fullIRI, shortName);
    }
    
    public static NoteType getComment() {
        return COMMENT;
    }
    

    private String replyTypeIRI;

    private String defaultShortName;

    // Empty constructor for the purposes of serialization
    private NoteType() {
    }

    private NoteType(String replyTypeIRI, String defaultShortName) {
        this.replyTypeIRI = replyTypeIRI;
        this.defaultShortName = defaultShortName;
    }


    public String getTypeIRI() {
        return replyTypeIRI;
    }

    public String getDefaultShortName() {
        return defaultShortName;
    }

    @Override
    public int hashCode() {
        return NoteType.class.getSimpleName().hashCode() + replyTypeIRI.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NoteType)) {
            return false;
        }
        NoteType other = (NoteType) obj;
        return this.replyTypeIRI.equals(other.replyTypeIRI);
    }

    public int compareTo(NoteType o) {
        if(this.replyTypeIRI.equals(o.replyTypeIRI)) {
            return 0;
        }
        else {
            return this.defaultShortName.compareTo(o.defaultShortName);
        }
    }
}
