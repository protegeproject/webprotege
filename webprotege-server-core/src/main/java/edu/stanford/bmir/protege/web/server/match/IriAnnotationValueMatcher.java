package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationValue;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class IriAnnotationValueMatcher implements Matcher<OWLAnnotationValue> {

    @Nonnull
    private final Matcher<IRI> iriMatcher;

    public IriAnnotationValueMatcher(@Nonnull Matcher<IRI> iriMatcher) {
        this.iriMatcher = checkNotNull(iriMatcher);
    }

    @Override
    public boolean matches(@Nonnull OWLAnnotationValue value) {
        return value instanceof IRI && iriMatcher.matches((IRI) value);
    }
}
