package edu.stanford.bmir.protege.web.server.owlapi.notes;

import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/04/2012
 */
public enum NotesVocabulary {

    NOTE_SUBJECT("subject"),

    NOTE_NOTE("note"),

    NOTE_TYPE("noteType"),

    TIME_STAMP("timeStamp"),

    NOTE_AUTHOR("author");

//    RELATED_ENTITY("relatedEntity");



    public static final String PREFIX = "http://protege.stanford.edu/vocabulary/notes#";
    
    public static final String DEFAULT_PREFIX_NAME = "notes:";


    private String localName;

    private IRI iri;
    
    private NotesVocabulary(String localName) {
        this.localName = localName;
        this.iri = IRI.create(PREFIX + localName);
    }

    public String getLocalName() {
        return localName;
    }

    public IRI getIRI() {
        return iri;
    }
}
