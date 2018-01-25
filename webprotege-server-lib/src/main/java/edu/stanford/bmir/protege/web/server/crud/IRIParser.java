package edu.stanford.bmir.protege.web.server.crud;

import org.semanticweb.owlapi.model.IRI;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 17/04/2014
 */
public class IRIParser {

    private static final String IRI_START_CHAR = "<";

    private static final String IRI_END_CHAR = ">";

    /**
     * Parses the specified lexical form into an IRI.  The lexical form must match the production for a complete IRI.
     * It must start with "&lt;", end with "&gt;" and the content between must be parsable into an IRI.
     * @param lexicalForm The lexical form.  Not {@code null}.
     * @return An IRI if the lexical form is that of a full IRI, otherwise
     * {@link Optional#empty()}.
     */
    public java.util.Optional<IRI> parseIRI(String lexicalForm) {
        if(lexicalForm.length() < 3) {
            return java.util.Optional.empty();
        }
        if(!hasIriStartChar(lexicalForm)) {
            return java.util.Optional.empty();
        }
        if(!hasIriEndChar(lexicalForm)) {
            return java.util.Optional.empty();
        }
        String iriContent = lexicalForm.substring(1, lexicalForm.length() - 1);
        if(!isValidIriContent(iriContent)) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(IRI.create(iriContent));
    }

    private boolean hasIriEndChar(String lexicalForm) {
        return lexicalForm.endsWith(IRI_END_CHAR);
    }

    private boolean hasIriStartChar(String lexicalForm) {
        return lexicalForm.startsWith(IRI_START_CHAR);
    }

    private boolean isValidIriContent(String content) {
        try {
            new URI(content);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
