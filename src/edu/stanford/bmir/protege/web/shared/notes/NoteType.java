package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.server.notes.api.NotesVocabulary;
import org.semanticweb.owlapi.model.IRI;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 * <p>
 *
 * </p>
 */
public final class NoteType implements Serializable, Comparable<NoteType> {
   
    private static final NoteType COMMENT = getReplyTypeFromShortName("Comment");


    public static NoteType getNoteTypeFromIRI(IRI iri) {
        return new NoteType(iri, iri.getFragment());
    }

    public static NoteType getReplyTypeFromShortName(String shortName) {
        IRI fullIRI = IRI.create(NotesVocabulary.NOTES_VOCABULARY_BASE + shortName);
        return new NoteType(fullIRI, shortName);
    }
    
    public static NoteType getComment() {
        return COMMENT;
    }
    

    private IRI noteTypeIRI;

    private String defaultShortName;

    // Empty constructor for the purposes of serialization
    private NoteType() {
    }

    private NoteType(IRI replyTypeIRI, String defaultShortName) {
        this.noteTypeIRI = replyTypeIRI;
        this.defaultShortName = defaultShortName;
    }


    public IRI getTypeIRI() {
        return noteTypeIRI;
    }

    public String getDefaultShortName() {
        return defaultShortName;
    }

    @Override
    public int hashCode() {
        return "NoteType".hashCode() + noteTypeIRI.hashCode();
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
        return this.noteTypeIRI.equals(other.noteTypeIRI);
    }

    public int compareTo(NoteType o) {
        if(this.noteTypeIRI.equals(o.noteTypeIRI)) {
            return 0;
        }
        else {
            return this.defaultShortName.compareTo(o.defaultShortName);
        }
    }
}
