package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class EntityAnnotationMatcher implements EntityFrameMatcher {

    @Nonnull
    private final HasAnnotationAssertionAxioms axiomProvider;

    @Nonnull
    private final Matcher<OWLAnnotation> annotationMatcher;

    public EntityAnnotationMatcher(@Nonnull HasAnnotationAssertionAxioms axiomProvider,
                                   @Nonnull Matcher<OWLAnnotation> annotationMatcher) {
        this.axiomProvider = checkNotNull(axiomProvider);
        this.annotationMatcher = checkNotNull(annotationMatcher);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity entity) {
        return axiomProvider.getAnnotationAssertionAxioms(entity.getIRI()).stream()
                            .map(OWLAnnotationAssertionAxiom::getAnnotation)
                            .anyMatch(annotationMatcher::matches);
    }
}
