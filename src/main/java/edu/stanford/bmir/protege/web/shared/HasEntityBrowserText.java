package edu.stanford.bmir.protege.web.shared;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public interface HasEntityBrowserText {

    /**
     * Gets the browser text for a specific entity.
     * @param entity The entity.  Not {@code null}.
     * @return An {@link Optional} which provides a {@link String} representig the browser text.
     * @throws NullPointerException if {@code entity} is {@code null}.
     */
    Optional<String> getBrowserText(OWLEntity entity);
}
