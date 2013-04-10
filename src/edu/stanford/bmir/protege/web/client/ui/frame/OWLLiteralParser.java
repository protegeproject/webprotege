package edu.stanford.bmir.protege.web.client.ui.frame;

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
     * @param text
     * @param language
     * @return
     * @throws OWLLiteralParseException
     */
    OWLLiteral parseLiteral(String text, Optional<String> language);
}
