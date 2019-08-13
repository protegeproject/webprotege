package edu.stanford.bmir.protege.web.server.frame;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-13
 */
class AxiomTranslator {

    private final OWLEntity subject;

    private final State initialState;

    private final EntitiesInProjectSignatureByIriIndex entitiesIndex;

    private final ContextRenderer ren;

    private final OWLAxiomVisitorExAdapter<Set<PropertyValue>> axiomVisitor = new OWLAxiomVisitorExAdapter<>(Collections
                                                                                                                     .emptySet()) {
        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLSubClassOfAxiom axiom) {
            return translateSubClassOf(axiom);
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLEquivalentClassesAxiom axiom) {
            return translateEquivalentClasses(axiom);
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLAnnotationAssertionAxiom axiom) {
            return translateAnnotationAssertion(axiom);
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLObjectPropertyAssertionAxiom axiom) {
            return translateObjectPropertyAssertion(axiom);
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLDataPropertyAssertionAxiom axiom) {
            return translateDataPropertyAssertion(axiom);
        }

        @Nonnull
        @Override
        public Set<PropertyValue> visit(OWLClassAssertionAxiom axiom) {
            return translateClassAssertion(axiom);
        }
    };

    @Inject
    @AutoFactory
    public AxiomTranslator(OWLEntity subject,
                    State initialState,
                    @Provided EntitiesInProjectSignatureByIriIndex entitiesIndex,
                    @Provided ContextRenderer ren) {
        this.subject = checkNotNull(subject);
        this.initialState = checkNotNull(initialState);
        this.entitiesIndex = checkNotNull(entitiesIndex);
        this.ren = checkNotNull(ren);
    }

    public Set<PropertyValue> translate(@Nonnull OWLAxiom axiom) {
        return axiom.accept(axiomVisitor);
    }

    private Set<PropertyValue> translateSubClassOf(OWLSubClassOfAxiom axiom) {
        if(axiom.getSubClass().equals(subject)) {
            return axiom.getSuperClass().accept(new ClassExpressionTranslator(initialState, ren));
        }
        else {
            return Collections.emptySet();
        }
    }



    private Set<PropertyValue> translateEquivalentClasses(OWLEquivalentClassesAxiom axiom) {
        if(!subject.isOWLClass()) {
            return Collections.emptySet();
        }
        Set<PropertyValue> result = new HashSet<>();
        if(axiom.contains(subject.asOWLClass())) {
            for(OWLClassExpression ce : axiom.getClassExpressions()) {
                if(!ce.equals(subject)) {
                    for(OWLClassExpression conj : ce.asConjunctSet()) {
                        Set<PropertyValue> vals = conj.accept(new ClassExpressionTranslator(State.DERIVED, ren));
                        if(vals != null) {
                            result.addAll(vals);
                        }
                    }
                }
            }
        }
        return result;
    }



    @Nonnull
    private Set<PropertyValue> translateAnnotationAssertion(OWLAnnotationAssertionAxiom axiom) {
        if(axiom.getSubject().equals(subject.getIRI())) {
            if(axiom.getValue() instanceof IRI) {
                var entities = entitiesIndex.getEntityInSignature((IRI) axiom.getValue()).collect(Collectors.toSet());
                if(!entities.isEmpty()) {
                    return entities
                            .stream()
                            .sorted()
                            .map(entity -> PropertyAnnotationValue.get(ren.getAnnotationPropertyData(axiom.getProperty()), ren
                                    .getAnnotationValueData(axiom.getValue()), State.ASSERTED))
                            .collect(Collectors.toSet());
                }
            }
            return Collections.singleton(PropertyAnnotationValue.get(ren.getAnnotationPropertyData(axiom.getProperty()), ren.getAnnotationValueData(axiom.getValue()), State.ASSERTED));

        }
        else {
            return null;
        }
    }


    @Nonnull
    private Set<PropertyValue> translateObjectPropertyAssertion(OWLObjectPropertyAssertionAxiom axiom) {
        if(axiom.getSubject().equals(subject) && !axiom.getProperty().isAnonymous() && !axiom.getObject().isAnonymous()) {
            return Collections.singleton(PropertyIndividualValue.get(ren.getObjectPropertyData(axiom
                                                                                                                    .getProperty()
                                                                                                                    .asOWLObjectProperty()), ren.getIndividualData(axiom
                                                                                                                                                                           .getObject()
                                                                                                                                                                           .asOWLNamedIndividual()), State.ASSERTED));
        }
        else {
            return null;
        }
    }

    @Nonnull
    private Set<PropertyValue> translateDataPropertyAssertion(OWLDataPropertyAssertionAxiom axiom) {
        if(axiom.getSubject().equals(subject)) {
            return Collections.singleton(PropertyLiteralValue.get(ren.getDataPropertyData(axiom
                                                                                                               .getProperty()
                                                                                                               .asOWLDataProperty()), OWLLiteralData
                                                                                       .get(axiom.getObject()), State.ASSERTED));
        }
        else {
            return null;
        }
    }

    @Nonnull
    private Set<PropertyValue> translateClassAssertion(OWLClassAssertionAxiom axiom) {
        if(axiom.getIndividual().equals(subject)) {
            return axiom.getClassExpression().accept(new ClassExpressionTranslator(initialState, ren));
        }
        else {
            return null;
        }
    }
}
