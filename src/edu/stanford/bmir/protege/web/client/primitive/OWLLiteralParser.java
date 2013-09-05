package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public interface OWLLiteralParser {

    /**
     * Parses the specified text and optional language into a literal.
     * @param text The text to be parsed.  Not {@code null}.
     * @param language The language.  Not {@code null}.
     * @return The {@link OWLLiteral} that {@code text} was parsed into.
     * @throws OWLLiteralParseException If the literal text is malformed.
     * @throws NullPointerException if {@code text} is {@code null} or {@code language} is {@code null}.
     */
    OWLLiteral parseLiteral(String text, Optional<String> language);
}
