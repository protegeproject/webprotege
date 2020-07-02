package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-23
 */
public class LangTagIsEmptyMatcher implements Matcher<OWLLiteral> {

    @Override
    public boolean matches(@Nonnull OWLLiteral value) {
        return value.getLang().isEmpty();
    }
}
