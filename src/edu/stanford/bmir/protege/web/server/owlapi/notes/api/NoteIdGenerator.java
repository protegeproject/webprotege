package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import org.semanticweb.owlapi.model.IRI;

import java.util.UUID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/08/2012
 */
public class NoteIdGenerator {
    
    private static final String NOTE_PATH_ELEMENT_NAME = "note";

    /**
     * Generates a fresh Note ID (IRI).  The generated IRI is based on the notes vocabulary base
     * {@link NotesVocabulary#NOTES_VOCABULARY_BASE}
     * plus a path name element plus a UUID.  The UUID is generated using {@link java.util.UUID#randomUUID()}.
     * @return The generated IRI. Not null.
     */
    public static NoteId createNoteId() {
        UUID uuid = UUID.randomUUID();
        StringBuilder sb = new StringBuilder();
        sb.append(NotesVocabulary.NOTES_VOCABULARY_BASE);
        sb.append(NOTE_PATH_ELEMENT_NAME);
        sb.append("/");
        sb.append(uuid.toString());
        return NoteId.createNoteIdFromLexicalForm(sb.toString());
    }

    /**
     * Creates a NoteId from a note id lexical form.
     * @param noteIdLexicalForm The lexical form from which to create the NoteId.
     * @return A NoteId that has the specified lexical form.  Not null.
     * @throws NullPointerException if noteIdLexicalForm is null.
     */
    public static NoteId createNoteIdFromLexicalForm(String noteIdLexicalForm) {
        if(noteIdLexicalForm == null) {
            throw new NullPointerException("noteIdLexicalForm must not be null");
        }
        return NoteId.createNoteIdFromLexicalForm(noteIdLexicalForm);
    }

    /**
     * Creates a NoteId from the lexical form of an IRI.
     * @param noteIdIRI The IRI from which to obtain the lexical form of the NoteId.  Not null.
     * @return A NoteId with the same lexical form as the specified IRI.  Not null.
     * @throws NullPointerException if the specified IRI is null.
     */
    public static NoteId createNoteIdFromIRI(IRI noteIdIRI) {
        if(noteIdIRI == null) {
            throw new NullPointerException("noteIdIRI must not be null");
        }
        return createNoteIdFromLexicalForm(noteIdIRI.toString());
    }
}
