package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class AnnotationMatcher implements Matcher<OWLAnnotation> {

    @Nonnull
    private final Matcher<OWLAnnotationProperty> propertyMatcher;

    @Nonnull
    private final Matcher<OWLAnnotationValue> annotationValueMatcher;

    public AnnotationMatcher(@Nonnull Matcher<OWLAnnotationProperty> propertyMatcher,
                             @Nonnull Matcher<OWLAnnotationValue> annotationValueMatcher) {
        this.propertyMatcher = checkNotNull(propertyMatcher);
        this.annotationValueMatcher = checkNotNull(annotationValueMatcher);
    }

    @Override
    public boolean matches(@Nonnull OWLAnnotation value) {
        return propertyMatcher.matches(value.getProperty())
                && annotationValueMatcher.matches(value.getValue());
    }
}
