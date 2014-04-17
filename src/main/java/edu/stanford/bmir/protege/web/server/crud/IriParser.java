package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.IRI;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 17/04/2014
 */
public class IRIParser {

    public static final String IRI_START_CHAR = "<";

    public static final String IRI_END_CHAR = ">";

    /**
     * Parses the specified lexical form into an IRI.  The lexical form must match the production for a complete IRI.
     * It must start with "&lt;", end with "&gt;" and the content between must be parsable into an IRI.
     * @param lexicalForm The lexical form.  Not {@code null}.
     * @return An IRI if the lexical form is that of a full IRI, otherwise
     * {@link com.google.common.base.Optional#absent()}.
     */
    public Optional<IRI> parseIRI(String lexicalForm) {
        if(lexicalForm.length() < 3) {
            return Optional.absent();
        }
        if(!hasIriStartChar(lexicalForm)) {
            return Optional.absent();
        }
        if(!hasIriEndChar(lexicalForm)) {
            return Optional.absent();
        }
        String iriContent = lexicalForm.substring(1, lexicalForm.length() - 1);
        if(!isValidIriContent(iriContent)) {
            return Optional.absent();
        }
        return Optional.of(IRI.create(iriContent));
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
