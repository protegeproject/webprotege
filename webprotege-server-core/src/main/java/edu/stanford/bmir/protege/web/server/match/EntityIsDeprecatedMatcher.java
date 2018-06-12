package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
public class EntityIsDeprecatedMatcher implements Matcher<OWLEntity> {

    private final Matcher<OWLEntity> delegate;

    public EntityIsDeprecatedMatcher(HasAnnotationAssertionAxioms axioms) {
        final AnnotationMatcher deprecatedMatcher = new AnnotationMatcher(
                OWLAnnotationProperty::isDeprecated,
                LiteralAnnotationValueMatcher.forIsXsdBooleanTrue()
        );
        delegate = new EntityAnnotationMatcher(axioms,
                                               deprecatedMatcher);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity value) {
        return delegate.matches(value);
    }
}
