package edu.stanford.bmir.protege.web.server.render;

import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
public class NullHighlightedEntityChecker implements HighlightedEntityChecker {

    @Override
    public boolean isHighlighted(OWLEntity entity) {
        return false;
    }
}
