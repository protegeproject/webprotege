package edu.stanford.bmir.protege.web.shared.notes;

import edu.stanford.bmir.protege.web.server.notes.api.NotesVocabulary;
import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 * <p>
 *
 * </p>
 */
@Deprecated
public enum NoteType {


    COMMENT("Comment");
    

    private String shortName;

    private IRI iri;

    // Empty constructor for the purposes of serialization
    NoteType() {
    }

    NoteType(String shortName) {
        this.shortName = shortName;
        this.iri = IRI.create(NotesVocabulary.NOTES_VOCABULARY_BASE + shortName);
    }

    public String getShortName() {
        return shortName;
    }

    public IRI getIRI() {
        return iri;
    }
}
