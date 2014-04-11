package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/01/2013
 */
public interface PrimitiveDataParser {

    /**
     * Parses the specified string into {@link edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData}.
     * @param text The string to be parsed.  Not {@code null}.
     * @param language The language tag.  Not {@code null}.
     * @param callback The callback to be notified of the result.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    void parsePrimitiveData(String text, Optional<String> language, Set<PrimitiveType> allowedTypes, PrimitiveDataParserCallback callback);
}
