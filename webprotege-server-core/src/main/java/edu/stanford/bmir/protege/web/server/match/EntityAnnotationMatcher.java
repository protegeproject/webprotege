package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import edu.stanford.bmir.protege.web.shared.match.AnnotationPresence;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import java.util.stream.Stream;

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

    @Nonnull
    private final AnnotationPresence annotationPresence;

    public EntityAnnotationMatcher(@Nonnull HasAnnotationAssertionAxioms axiomProvider,
                                   @Nonnull Matcher<OWLAnnotation> annotationMatcher,
                                   @Nonnull AnnotationPresence annotationPresence) {
        this.axiomProvider = checkNotNull(axiomProvider);
        this.annotationMatcher = checkNotNull(annotationMatcher);
        this.annotationPresence = annotationPresence;
    }

    @Override
    public boolean matches(@Nonnull OWLEntity entity) {
        Stream<OWLAnnotation> annotationStream = axiomProvider.getAnnotationAssertionAxioms(entity.getIRI()).stream()
                                                                 .map(OWLAnnotationAssertionAxiom::getAnnotation);
//        if (annotationPresence == AnnotationPresence.PRESENT) {
            return annotationStream.anyMatch(annotationMatcher::matches);
//        }
//        else {
//            return annotationStream.noneMatch(annotationMatcher::matches);
//        }
    }
}
