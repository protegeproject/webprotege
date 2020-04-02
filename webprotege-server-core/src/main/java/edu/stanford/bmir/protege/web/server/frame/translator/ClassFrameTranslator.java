package edu.stanford.bmir.protege.web.server.frame.translator;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.frame.Mode;
import edu.stanford.bmir.protege.web.server.frame.PropertyValueMinimiser;
import edu.stanford.bmir.protege.web.server.hierarchy.HasGetAncestors;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.EquivalentClassesAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.SubClassOfAxiomsBySubClassIndex;
import edu.stanford.bmir.protege.web.server.match.RelationshipMatcherFactory;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrameTranslatorOptions;
import edu.stanford.bmir.protege.web.shared.frame.PlainClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import edu.stanford.bmir.protege.web.shared.match.criteria.RelationshipCriteria;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.shared.frame.ClassFrameTranslatorOptions.AncestorsTreatment.INCLUDE_ANCESTORS;
import static edu.stanford.bmir.protege.web.shared.frame.RelationshipTranslationOptions.RelationshipMinification.MINIMIZED_RELATIONSHIPS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 * <p>
 * A translator that converts sets of axioms to class frames and vice-versa.
 * </p>
 */
public class ClassFrameTranslator {

    @Nonnull
    private final ProjectOntologiesIndex ontologiesIndex;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex;

    @Nonnull
    private final EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsIndex;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final HasGetAncestors<OWLClass> ancestorsProvider;

    @Nonnull
    private final PropertyValueMinimiser propertyValueMinimiser;

    @Nonnull
    private final AxiomPropertyValueTranslator axiomPropertyValueTranslator;

    @Nonnull
    private final ClassFrameTranslatorOptions options;

    @Nonnull
    private final RelationshipMatcherFactory matcherFactory;

    @AutoFactory
    @Inject
    public ClassFrameTranslator(@Provided @Nonnull ProjectOntologiesIndex ontologiesIndex,
                                @Provided @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex,
                                @Provided @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex,
                                @Provided @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsIndex,
                                @Provided @Nonnull OWLDataFactory dataFactory,
                                @Provided @Nonnull HasGetAncestors<OWLClass> ancestorsProvider,
                                @Provided @Nonnull PropertyValueMinimiser propertyValueMinimiser,
                                @Provided @Nonnull AxiomPropertyValueTranslator axiomPropertyValueTranslator,
                                @Nonnull ClassFrameTranslatorOptions options,
                                @Nonnull @Provided RelationshipMatcherFactory matcherFactory) {
        this.ontologiesIndex = ontologiesIndex;
        this.subClassOfAxiomsIndex = checkNotNull(subClassOfAxiomsIndex);
        this.equivalentClassesAxiomsIndex = checkNotNull(equivalentClassesAxiomsIndex);
        this.annotationAssertionAxiomsIndex = checkNotNull(annotationAssertionAxiomsIndex);
        this.dataFactory = checkNotNull(dataFactory);
        this.ancestorsProvider = checkNotNull(ancestorsProvider);
        this.propertyValueMinimiser = checkNotNull(propertyValueMinimiser);
        this.axiomPropertyValueTranslator = checkNotNull(axiomPropertyValueTranslator);
        this.options = checkNotNull(options);
        this.matcherFactory = checkNotNull(matcherFactory);
    }

    @Nonnull
    public Set<OWLAxiom> getAxioms(@Nonnull PlainClassFrame frame, @Nonnull Mode mode) {
        return translateToAxioms(frame.getSubject(), frame, mode);
    }

    private Set<OWLAxiom> translateToAxioms(OWLClass subject, PlainClassFrame classFrame, Mode mode) {
        var result = new HashSet<OWLAxiom>();
        for(OWLClass parent : classFrame.getParents()) {
            result.add(dataFactory.getOWLSubClassOfAxiom(subject, parent));
        }
        for(PlainPropertyValue propertyValue : classFrame.getPropertyValues()) {
            result.addAll(axiomPropertyValueTranslator.getAxioms(subject, propertyValue, mode));
        }
        return result;
    }

    @Nonnull
    public PlainClassFrame getFrame(@Nonnull OWLClass subject) {
        return translateToClassFrame(subject);
    }

    private PlainClassFrame translateToClassFrame(OWLClass cls) {
        var relevantAxioms = getRelevantAxioms(cls, true);
        var propertyValues = new ArrayList<>(translateAxiomsToPropertyValues(cls,
                                                                             relevantAxioms,
                                                                             State.ASSERTED));
        if(options.getAncestorsTreatment() == INCLUDE_ANCESTORS) {
            for(OWLClass ancestor : ancestorsProvider.getAncestors(cls)) {
                if(!ancestor.equals(cls)) {
                    propertyValues.addAll(translateAxiomsToPropertyValues(ancestor,
                                                                          getRelevantAxioms(ancestor, false),
                                                                          State.DERIVED));
                }
            }
        }

        var parents = getSubClassOfAxioms(cls)
                .map(OWLSubClassOfAxiom::getSuperClass)
                .filter(OWLClassExpression::isNamed)
                .map(OWLClassExpression::asOWLClass)
                .distinct()
                .collect(toImmutableSet());

        var propertyValuesMin = ImmutableSet.copyOf(propertyValues);


        if(options.getRelationshipTranslationOptions()
                  .getRelationshipMinification() == MINIMIZED_RELATIONSHIPS) {
            propertyValuesMin = propertyValueMinimiser.minimisePropertyValues(propertyValues)
                                                      .collect(toImmutableSet());
        }

        return PlainClassFrame.get(cls,
                                   parents,
                                   propertyValuesMin);
    }

    private Set<OWLAxiom> getRelevantAxioms(OWLClass subject,
                                            boolean includeAnnotations) {
        var subClassOfAxioms = getSubClassOfAxioms(subject);
        var equivalentClassesAxioms = getEquivalentClassesAxioms(subject);
        var annotationAssertions = getAnnotationAssertions(subject, includeAnnotations);
        return Stream.of(subClassOfAxioms,
                         equivalentClassesAxioms,
                         annotationAssertions)
                     .flatMap(ax -> ax)
                     .collect(toImmutableSet());
    }

    private List<PlainPropertyValue> translateAxiomsToPropertyValues(OWLClass subject,
                                                                     Set<OWLAxiom> relevantAxioms,
                                                                     State initialState) {
        var relationshipOptions = options.getRelationshipTranslationOptions();
        Optional<RelationshipCriteria> outgoingRelationshipCriteria = relationshipOptions.getOutgoingRelationshipCriteria();
        if(outgoingRelationshipCriteria
                .isEmpty()) {
            return ImmutableList.of();
        }
        var relationshipMatcher = matcherFactory.getRelationshipMatcher(outgoingRelationshipCriteria.get());
        return relevantAxioms.stream()
                             .flatMap(axiom -> axiomPropertyValueTranslator.getPropertyValues(subject,
                                                                                              axiom,
                                                                                              initialState)
                                                                           .stream())
                             .filter(relationshipMatcher::matches)
                             .collect(Collectors.toList());
    }

    private Stream<OWLSubClassOfAxiom> getSubClassOfAxioms(OWLClass subject) {
        return ontologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> subClassOfAxiomsIndex.getSubClassOfAxiomsForSubClass(subject, ontId));
    }

    private Stream<OWLEquivalentClassesAxiom> getEquivalentClassesAxioms(OWLClass subject) {
        return ontologiesIndex.getOntologyIds()
                              .flatMap(ontId -> equivalentClassesAxiomsIndex.getEquivalentClassesAxioms(subject,
                                                                                                        ontId));
    }

    private Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertions(OWLClass subject, boolean includeAnnotations) {
        if(!includeAnnotations) {
            return Stream.empty();
        }
        return ontologiesIndex.getOntologyIds()
                              .flatMap(ontId -> annotationAssertionAxiomsIndex.getAxiomsForSubject(subject.getIRI(),
                                                                                                   ontId));
    }
}
