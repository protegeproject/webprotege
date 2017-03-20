package edu.stanford.bmir.protege.web.server.notes.impl.chao;

import edu.stanford.bmir.protege.web.server.notes.api.NotesVocabulary;
import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
@Deprecated
public enum ChAOVocabulary {

    ANNOTATES("annotates"),
    
    CREATED_AT("createdAt"),
    
    AUTHOR("author"),
    
    SUBJECT("subject"),
    
    BODY("body");

    private String localName;

    private IRI iri;

    ChAOVocabulary(String localName) {
        this.localName = localName;
        this.iri = IRI.create(NotesVocabulary.NOTES_VOCABULARY_BASE + localName);
    }

    public String getLocalName() {
        return localName;
    }

    public IRI getIRI() {
        return iri;
    }
}
