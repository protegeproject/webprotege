package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class LiteralAnnotationValueMatcher implements Matcher<OWLAnnotationValue> {

    @Nonnull
    private final Matcher<OWLLiteral> literalMatcher;

    public LiteralAnnotationValueMatcher(@Nonnull Matcher<OWLLiteral> literalMatcher) {
        this.literalMatcher = checkNotNull(literalMatcher);
    }

    @Override
    public boolean matches(@Nonnull OWLAnnotationValue value) {
        return value.isLiteral() && literalMatcher.matches((OWLLiteral) value);
    }
}
