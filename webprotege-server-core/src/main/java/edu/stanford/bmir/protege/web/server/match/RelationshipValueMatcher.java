package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public class RelationshipValueMatcher implements Matcher<OWLPrimitive> {

    @Override
    public boolean matches(@Nonnull OWLPrimitive value) {
        return false;
    }
}
