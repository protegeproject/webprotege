package edu.stanford.bmir.protege.web.server.match;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsIndex;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@AutoFactory
public class IriAnnotationsMatcher implements Matcher<IRI> {

    @Nonnull
    private final AnnotationAssertionAxiomsIndex axiomProvider;

    @Nonnull
    private final Matcher<OWLAnnotation> annotationMatcher;

    @Inject
    public IriAnnotationsMatcher(@Nonnull @Provided AnnotationAssertionAxiomsIndex axiomProvider,
                                 @Nonnull Matcher<OWLAnnotation> annotationMatcher) {
        this.axiomProvider = checkNotNull(axiomProvider);
        this.annotationMatcher = checkNotNull(annotationMatcher);
    }

    @Override
    public boolean matches(@Nonnull IRI subject) {
        return axiomProvider.getAnnotationAssertionAxioms(subject)
                            .map(OWLAnnotationAssertionAxiom::getAnnotation)
                            .anyMatch(annotationMatcher::matches);
    }
}
