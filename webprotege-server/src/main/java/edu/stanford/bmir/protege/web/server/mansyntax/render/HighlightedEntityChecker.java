package edu.stanford.bmir.protege.web.server.mansyntax.render;

import org.semanticweb.owlapi.model.OWLEntity;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 27/01/15
*/
public interface HighlightedEntityChecker {

    /**
     * Determines if the given entity should be highlighted.
     * @param entity The entity.  Not {@code null}.
     * @return {@code true} if the given entity should be highlighted, otherwise {@code false}.
     */
    boolean isHighlighted(OWLEntity entity);
}
