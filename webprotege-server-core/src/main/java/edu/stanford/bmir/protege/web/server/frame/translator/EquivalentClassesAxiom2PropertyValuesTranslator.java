package edu.stanford.bmir.protege.web.server.frame.translator;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-02
 */
class EquivalentClassesAxiom2PropertyValuesTranslator {

    @Inject
    public EquivalentClassesAxiom2PropertyValuesTranslator(@Nonnull ClassExpression2PropertyValuesTranslator classExpression2PropertyValuesTranslator) {
        this.classExpression2PropertyValuesTranslator = classExpression2PropertyValuesTranslator;
    }

    @Nonnull
    private final ClassExpression2PropertyValuesTranslator classExpression2PropertyValuesTranslator;

    @Nonnull
    public Set<PlainPropertyValue> translate(@Nonnull OWLEquivalentClassesAxiom axiom,
                                             @Nonnull OWLEntity subject,
                                             @Nonnull State initialState) {
        if(!subject.isOWLClass()) {
            return ImmutableSet.of();
        }
        var classExpressions = axiom.getClassExpressions();
        if(!classExpressions.contains(subject.asOWLClass())) {
            return ImmutableSet.of();
        }
        return classExpressions.stream()
                               .filter(ce -> !ce.equals(subject))
                               .flatMap(this::toDerivedPlainPropertyValues)
                               .collect(toImmutableSet());
    }

    private Stream<? extends PlainPropertyValue> toDerivedPlainPropertyValues(OWLClassExpression ce) {
        return classExpression2PropertyValuesTranslator.translate(State.DERIVED, ce)
                                                       .stream();
    }
}
