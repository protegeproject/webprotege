package edu.stanford.bmir.protege.web.server.match;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.match.AnnotationPresence;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
@AutoFactory
public class EntityAnnotationMatcher implements EntityFrameMatcher {

    @Nonnull
    private final AnnotationAssertionAxiomsIndex axiomProvider;

    @Nonnull
    private final Matcher<OWLAnnotation> annotationMatcher;

    @Nonnull
    private final AnnotationPresence annotationPresence;

    @Inject
    public EntityAnnotationMatcher(@Nonnull @Provided AnnotationAssertionAxiomsIndex axiomProvider,
                                   @Nonnull Matcher<OWLAnnotation> annotationMatcher,
                                   @Nonnull AnnotationPresence annotationPresence) {
        this.axiomProvider = checkNotNull(axiomProvider);
        this.annotationMatcher = checkNotNull(annotationMatcher);
        this.annotationPresence = checkNotNull(annotationPresence);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity entity) {
        Stream<OWLAnnotation> annotationStream = axiomProvider.getAnnotationAssertionAxioms(entity.getIRI())
                                                                 .map(EntityAnnotationMatcher::getAnnotation);
        if (annotationPresence == AnnotationPresence.AT_LEAST_ONE) {
            return annotationStream.anyMatch(annotationMatcher::matches);
        }
        else if(annotationPresence == AnnotationPresence.AT_MOST_ONE) {
            return annotationStream.filter(annotationMatcher::matches)
                                   .limit(2)
                                   .count() <= 1;
        }
        else {
            return annotationStream.noneMatch(annotationMatcher::matches);
        }
    }

    private static OWLAnnotation getAnnotation(@Nonnull OWLAnnotationAssertionAxiom axiom) {
        return new LightweightAnnotation(axiom);
    }

    private static class LightweightAnnotation implements OWLAnnotation {

        @Nonnull
        private final OWLAnnotationAssertionAxiom axiom;

        public LightweightAnnotation(@Nonnull OWLAnnotationAssertionAxiom axiom) {
            this.axiom = axiom;
        }

        @Nonnull
        @Override
        public OWLAnnotationProperty getProperty() {
            return axiom.getProperty();
        }

        @Nonnull
        @Override
        public OWLAnnotationValue getValue() {
            return axiom.getValue();
        }

        @Override
        public boolean isDeprecatedIRIAnnotation() {
            return axiom.isDeprecatedIRIAssertion();
        }

        @Nonnull
        @Override
        public Set<OWLAnnotation> getAnnotations() {
            return Collections.emptySet();
        }

        @Nonnull
        @Override
        public OWLAnnotation getAnnotatedAnnotation(@Nonnull Set<OWLAnnotation> annotations) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void accept(@Nonnull OWLAnnotationObjectVisitor visitor) {
            visitor.visit(this);
        }

        @Nonnull
        @Override
        public <O> O accept(@Nonnull OWLAnnotationObjectVisitorEx<O> visitor) {
            return visitor.visit(this);
        }

        @Override
        public int compareTo(OWLObject o) {
            throw new UnsupportedOperationException();
        }

        @Nonnull
        @Override
        public Set<OWLAnnotationProperty> getAnnotationPropertiesInSignature() {
            return Collections.singleton(axiom.getProperty());
        }

        @Nonnull
        @Override
        public Set<OWLClassExpression> getNestedClassExpressions() {
            return Collections.emptySet();
        }

        @Override
        public void accept(@Nonnull OWLObjectVisitor visitor) {
            visitor.visit(this);
        }

        @Nonnull
        @Override
        public <O> O accept(@Nonnull OWLObjectVisitorEx<O> visitor) {
            return visitor.visit(this);
        }

        @Override
        public boolean isTopEntity() {
            return false;
        }

        @Override
        public boolean isBottomEntity() {
            return false;
        }

        @Nonnull
        @Override
        public Set<OWLAnonymousIndividual> getAnonymousIndividuals() {
            return Collections.emptySet();
        }

        @Nonnull
        @Override
        public Set<OWLClass> getClassesInSignature() {
            return Collections.emptySet();
        }

        @Override
        public boolean containsEntityInSignature(@Nonnull OWLEntity owlEntity) {
            return owlEntity.isOWLAnnotationProperty() && axiom.getProperty().equals(owlEntity);
        }

        @Nonnull
        @Override
        public Set<OWLDataProperty> getDataPropertiesInSignature() {
            return Collections.emptySet();
        }

        @Nonnull
        @Override
        public Set<OWLDatatype> getDatatypesInSignature() {
            return axiom.getValue().getDatatypesInSignature();
        }

        @Nonnull
        @Override
        public Set<OWLNamedIndividual> getIndividualsInSignature() {
            return Collections.emptySet();
        }

        @Nonnull
        @Override
        public Set<OWLObjectProperty> getObjectPropertiesInSignature() {
            return Collections.emptySet();
        }

        @Nonnull
        @Override
        public Set<OWLEntity> getSignature() {
            return Collections.singleton(axiom.getProperty());
        }
    }
}
