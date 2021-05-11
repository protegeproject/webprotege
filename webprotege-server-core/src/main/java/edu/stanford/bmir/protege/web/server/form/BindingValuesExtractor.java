package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.match.MatcherFactory;
import edu.stanford.bmir.protege.web.shared.form.data.FormEntitySubject;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrameTranslationOptions;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static dagger.internal.codegen.DaggerStreams.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-24
 */
public class BindingValuesExtractor {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertionAxiomsBySubjectIndex;

    @Nonnull
    private final DataPropertyAssertionAxiomsBySubjectIndex dataPropertyAssertionAxiomsBySubjectIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex;

    @Nonnull
    private final ClassAssertionAxiomsByClassIndex classAssertionAxiomsByClassIndex;

    @Nonnull
    private final ClassFrameProvider classFrameProvider;

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Inject
    public BindingValuesExtractor(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                  @Nonnull ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex,
                                  @Nonnull ClassHierarchyProvider classHierarchyProvider,
                                  @Nonnull ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertionAxiomsBySubjectIndex,
                                  @Nonnull DataPropertyAssertionAxiomsBySubjectIndex dataPropertyAssertionAxiomsBySubjectIndex,
                                  @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex,
                                  @Nonnull ClassAssertionAxiomsByClassIndex classAssertionAxiomsByClassIndex,
                                  @Nonnull ClassFrameProvider classFrameProvider,
                                  @Nonnull MatcherFactory matcherFactory) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.classAssertionAxiomsByIndividualIndex = classAssertionAxiomsByIndividualIndex;
        this.classHierarchyProvider = classHierarchyProvider;
        this.objectPropertyAssertionAxiomsBySubjectIndex = objectPropertyAssertionAxiomsBySubjectIndex;
        this.dataPropertyAssertionAxiomsBySubjectIndex = dataPropertyAssertionAxiomsBySubjectIndex;
        this.annotationAssertionAxiomsBySubjectIndex = annotationAssertionAxiomsBySubjectIndex;
        this.classAssertionAxiomsByClassIndex = classAssertionAxiomsByClassIndex;
        this.classFrameProvider = classFrameProvider;
        this.matcherFactory = matcherFactory;
    }

    @Nonnull
    public ImmutableList<OWLPrimitive> getBindingValues(@Nonnull Optional<FormEntitySubject> formSubject,
                                                        @Nonnull OwlBinding binding) {
        return formSubject.map(s -> getBindingValues(s, binding)).orElseGet(ImmutableList::of);
    }


    private ImmutableList<OWLPrimitive> getBindingValues(@Nonnull FormEntitySubject subject,
                                                         @Nonnull OwlBinding binding) {
        OWLEntityVisitorEx<ImmutableList<OWLPrimitive>> subjectVisitor = new OWLEntityVisitorEx<>() {
            @Nonnull
            @Override
            public ImmutableList<OWLPrimitive> visit(@Nonnull OWLClass cls) {
                return getBindingsForClass(cls, binding);
            }

            @Nonnull
            @Override
            public ImmutableList<OWLPrimitive> visit(@Nonnull OWLObjectProperty property) {
                return ImmutableList.of();
            }

            @Nonnull
            @Override
            public ImmutableList<OWLPrimitive> visit(@Nonnull OWLDataProperty property) {
                return ImmutableList.of();
            }

            @Nonnull
            @Override
            public ImmutableList<OWLPrimitive> visit(@Nonnull OWLNamedIndividual individual) {
                return getBindingsForIndividual(individual, binding);
            }

            @Nonnull
            @Override
            public ImmutableList<OWLPrimitive> visit(@Nonnull OWLDatatype datatype) {
                return ImmutableList.of();
            }

            @Nonnull
            @Override
            public ImmutableList<OWLPrimitive> visit(@Nonnull OWLAnnotationProperty property) {
                return ImmutableList.of();
            }
        };
        return subject.getEntity().accept(subjectVisitor);
    }

    @Nonnull
    private ImmutableList<OWLPrimitive> getBindingsForClass(@Nonnull OWLClass subject, @Nonnull OwlBinding binding) {
        if (binding instanceof OwlPropertyBinding) {
            var propertyBinding = (OwlPropertyBinding) binding;
            var valuesFilterPredicate = getValuesFilterPredicate(propertyBinding);
            var property = ((OwlPropertyBinding) binding).getProperty();
            if (property.isOWLAnnotationProperty()) {
                return projectOntologiesIndex.getOntologyIds()
                                             .flatMap(ontId -> annotationAssertionAxiomsBySubjectIndex.getAxiomsForSubject(
                                                     subject.getIRI(),
                                                     ontId))
                                             .filter(ax -> ax.getProperty().equals(property))
                                             .map(OWLAnnotationAssertionAxiom::getValue)
                                             .filter(valuesFilterPredicate)
                                             .sorted()
                                             .collect(toImmutableList());
            }
            // Fallback to frame for non-annotation assertions, for now
            return classFrameProvider.getFrame(subject, ClassFrameTranslationOptions.defaultOptions())
                                     .getPropertyValues()
                                     .stream()
                                     .filter(pv -> pv.getProperty().equals(property))
                                     .map(PlainPropertyValue::getValue)
                                     .filter(valuesFilterPredicate)
                                     .sorted()
                                     .collect(toImmutableList());
        }
        else if (binding instanceof OwlClassBinding) {
            return classHierarchyProvider.getParents(subject).stream().sorted().collect(toImmutableList());

        }
        else if (binding instanceof OwlInstanceBinding) {
            return projectOntologiesIndex.getOntologyIds()
                                         .flatMap(ontId -> classAssertionAxiomsByClassIndex.getClassAssertionAxioms(
                                                 subject,
                                                 ontId))
                                         .map(OWLClassAssertionAxiom::getIndividual)
                                         .filter(OWLIndividual::isNamed)
                                         .map(OWLIndividual::asOWLNamedIndividual)
                                         .sorted()
                                         .collect(toImmutableList());

        }
        else if (binding instanceof OwlSubClassBinding) {
            return classHierarchyProvider.getChildren(subject).stream().sorted().collect(toImmutableList());
        }
        else {
            return ImmutableList.of();
        }
    }

    @Nonnull
    private ImmutableList<OWLPrimitive> getBindingsForIndividual(@Nonnull OWLNamedIndividual individual,
                                                                 @Nonnull OwlBinding binding) {
        if (binding instanceof OwlPropertyBinding) {
            var propertyBinding = (OwlPropertyBinding) binding;
            var valuesFilterPredicate = getValuesFilterPredicate(propertyBinding);
            var property = propertyBinding.getProperty();
            if (property.isOWLAnnotationProperty()) {
                return projectOntologiesIndex.getOntologyIds()
                                             .flatMap(ontId -> annotationAssertionAxiomsBySubjectIndex.getAxiomsForSubject(
                                                     individual.getIRI(),
                                                     ontId))
                                             .filter(ax -> ax.getProperty().equals(property))
                                             .map(OWLAnnotationAssertionAxiom::getValue)
                                             .filter(valuesFilterPredicate)
                                             .sorted()
                                             .collect(toImmutableList());
            }
            else if (property.isOWLDataProperty()) {
                var assertionBased = getIndividualDataPropertyAssertionBasedValues(individual, property);
                var subClassOfBased = getIndividualSubClassOfBasedValues(individual, property);
                return Streams.concat(assertionBased, subClassOfBased)
                              .filter(valuesFilterPredicate)
                              .distinct()
                              .collect(toImmutableList());
            }
            else if (property.isOWLObjectProperty()) {
                var assertionBased = getIndividualObjectPropertyAssertionBasedValues(individual, property);
                var subClassOfBased = getIndividualSubClassOfBasedValues(individual, property);
                return Streams.concat(assertionBased, subClassOfBased)
                              .filter(valuesFilterPredicate)
                              .distinct()
                              .collect(toImmutableList());
            }
            else {
                // Shouldn't happen
                return ImmutableList.of();
            }
        }
        else if (binding instanceof OwlClassBinding) {
            return projectOntologiesIndex.getOntologyIds()
                                         .flatMap(ontId -> classAssertionAxiomsByIndividualIndex.getClassAssertionAxioms(
                                                 individual,
                                                 ontId))
                                         .map(OWLClassAssertionAxiom::getClassExpression)
                                         .filter(OWLClassExpression::isNamed)
                                         .distinct()
                                         .map(OWLClassExpression::asOWLClass)
                                         .sorted()
                                         .collect(toImmutableList());

        }
        else {
            return ImmutableList.of();
        }
    }

    private Predicate<OWLPrimitive> getValuesFilterPredicate(OwlPropertyBinding propertyBinding) {
        var matcher = propertyBinding.getValuesCriteria()
                                     .map(matcherFactory::getRelationshipValueMatcher)
                                     .orElse(o -> true);
        return matcher::matches;
    }

    private Stream<OWLLiteral> getIndividualDataPropertyAssertionBasedValues(@Nonnull OWLNamedIndividual individual,
                                                                             OWLProperty property) {
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> dataPropertyAssertionAxiomsBySubjectIndex.getDataPropertyAssertions(
                                             individual,
                                             ontId))
                                     .filter(ax -> ax.getProperty().equals(property))
                                     .map(OWLDataPropertyAssertionAxiom::getObject)
                                     .sorted();
    }

    private Stream<OWLNamedIndividual> getIndividualObjectPropertyAssertionBasedValues(@Nonnull OWLNamedIndividual individual,
                                                                                       OWLProperty property) {
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> objectPropertyAssertionAxiomsBySubjectIndex.getObjectPropertyAssertions(
                                             individual,
                                             ontId))
                                     .filter(ax -> ax.getProperty().equals(property))
                                     .map(OWLObjectPropertyAssertionAxiom::getObject)
                                     .filter(OWLIndividual::isNamed)
                                     .map(OWLIndividual::asOWLNamedIndividual);
    }

    private Stream<OWLPrimitive> getIndividualSubClassOfBasedValues(@Nonnull OWLNamedIndividual individual,
                                                                    OWLProperty property) {
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> classAssertionAxiomsByIndividualIndex.getClassAssertionAxioms(
                                             individual,
                                             ontId))
                                     .map(OWLClassAssertionAxiom::getClassExpression)
                                     .filter(ce -> ce instanceof OWLObjectRestriction)
                                     .map(ce -> (OWLRestriction) ce)
                                     .filter(ce -> ce.getProperty().equals(property))
                                     .filter(ce -> ce instanceof OWLObjectSomeValuesFrom || ce instanceof OWLObjectHasValue || ce instanceof OWLDataSomeValuesFrom || ce instanceof OWLDataHasValue)
                                     .map(ce -> {
                                         if (ce instanceof OWLObjectSomeValuesFrom) {
                                             return ((OWLObjectSomeValuesFrom) ce).getFiller();
                                         }
                                         else if (ce instanceof OWLObjectHasValue) {
                                             return ((OWLObjectHasValue) ce).getFiller();
                                         }
                                         else if (ce instanceof OWLDataHasValue) {
                                             return ((OWLDataHasValue) ce).getFiller();
                                         }
                                         else {
                                             return ((OWLDataSomeValuesFrom) ce).getFiller();
                                         }
                                     })
                                     .filter(filler -> filler instanceof OWLPrimitive)
                                     .map(filler -> (OWLPrimitive) filler);
    }
}
