package edu.stanford.bmir.protege.web.server.frame.translator;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.frame.ClassFrameProvider;
import edu.stanford.bmir.protege.web.server.frame.Mode;
import edu.stanford.bmir.protege.web.server.frame.PropertyValueMinimiser;
import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByIndividualIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.PropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.SameIndividualAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/12/2012
 */
public class NamedIndividualFrameTranslator {


    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ClassAssertionAxiomsByIndividualIndex classAssertionsByIndividual;

    @Nonnull
    private final PropertyAssertionAxiomsBySubjectIndex assertionsBySubject;

    @Nonnull
    private final SameIndividualAxiomsIndex sameIndividualAxiomsIndex;

    @Nonnull
    private final PropertyValueMinimiser propertyValueMinimiser;

    @Nonnull
    private final ClassFrameProvider classFrameProvider;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    @Nonnull
    private final OWLDataFactory dataFactory;

    private boolean minimizePropertyValues = false;

    @Inject
    public NamedIndividualFrameTranslator(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                          @Nonnull ClassAssertionAxiomsByIndividualIndex classAssertionsByIndividual,
                                          @Nonnull PropertyAssertionAxiomsBySubjectIndex assertionsBySubject,
                                          @Nonnull SameIndividualAxiomsIndex sameIndividualAxiomsIndex,
                                          @Nonnull PropertyValueMinimiser propertyValueMinimiser,
                                          @Nonnull ClassFrameProvider classFrameProvider,
                                          @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider,
                                          @Nonnull OWLDataFactory dataFactory) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.classAssertionsByIndividual = checkNotNull(classAssertionsByIndividual);
        this.assertionsBySubject = checkNotNull(assertionsBySubject);
        this.sameIndividualAxiomsIndex = checkNotNull(sameIndividualAxiomsIndex);
        this.propertyValueMinimiser = checkNotNull(propertyValueMinimiser);
        this.axiomPropertyValueTranslatorProvider = checkNotNull(axiomPropertyValueTranslatorProvider);
        this.classFrameProvider = checkNotNull(classFrameProvider);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Nonnull
    public PlainNamedIndividualFrame getFrame(@Nonnull OWLNamedIndividual subject) {
        return translateToNamedIndividualFrame(subject, true);
    }

    @Nonnull
    public PlainNamedIndividualFrame getFrame(@Nonnull OWLNamedIndividual subject,
                                         boolean includeDerivedInformation) {
        return translateToNamedIndividualFrame(subject, includeDerivedInformation);
    }

    public void setMinimizePropertyValues(boolean mimimizePropertyValues) {
        this.minimizePropertyValues = mimimizePropertyValues;
    }

    private PlainNamedIndividualFrame translateToNamedIndividualFrame(OWLNamedIndividual subjectindividual,
                                                                 boolean includeDerived) {
        var relevantAxioms = getRelevantAxioms(subjectindividual);

        var types = relevantAxioms
                .stream()
                .filter(OWLClassAssertionAxiom.class::isInstance)
                .map(OWLClassAssertionAxiom.class::cast)
                .map(OWLClassAssertionAxiom::getClassExpression)
                .filter(OWLClassExpression::isNamed)
                .map(OWLClassExpression::asOWLClass)
                .sorted()
                .collect(toImmutableSet());

        var assertedPropertyValues =
                relevantAxioms.stream()
                .flatMap(ax -> toAssertedPropertyValues(subjectindividual, ax));

        Stream<PlainPropertyValue> derivedPropertyValues;

        if(includeDerived) {
            derivedPropertyValues = getClassAssertionAxioms(subjectindividual)
                    .map(OWLClassAssertionAxiom::getClassExpression)
                    .filter(OWLClassExpression::isNamed)
                    .map(OWLClassExpression::asOWLClass)
                    .map(this::getClassFrame)
                    .flatMap(translator -> translator.getPropertyValues().stream())
                    .filter(PlainPropertyValue::isLogical)
                    .map(propertyValue -> propertyValue.withState(State.DERIVED));
        }
        else {
            derivedPropertyValues = Stream.empty();
        }

        var propertyValues =
                Streams.concat(assertedPropertyValues, derivedPropertyValues)
                .collect(toImmutableList());

        var propertyValuesMin = ImmutableSet.copyOf(propertyValues);
        if(minimizePropertyValues) {
            propertyValuesMin = propertyValueMinimiser.minimisePropertyValues(propertyValues)
                                                      .collect(toImmutableSet());
        }

        var sameIndividuals = getSameIndividualAxioms(subjectindividual)
                .flatMap(ax -> ax.getIndividuals().stream())
                .filter(OWLIndividual::isNamed)
                .filter(ind -> !ind.equals(subjectindividual))
                .map(OWLIndividual::asOWLNamedIndividual)
                .sorted()
                .collect(toImmutableSet());

        return PlainNamedIndividualFrame.get(subjectindividual,
                                             types,
                                             sameIndividuals,
                                             propertyValuesMin);
    }

    @Nonnull
    private PlainClassFrame getClassFrame(@Nonnull OWLClass subject) {
        ClassFrameTranslatorOptions options = ClassFrameTranslatorOptions.get(
                ClassFrameTranslatorOptions.AncestorsTreatment.INCLUDE_ANCESTORS,
                RelationshipTranslationOptions.get(
                        RelationshipTranslationOptions.allOutgoingRelationships(),
                        RelationshipTranslationOptions.noIncomingRelationships(),
                        RelationshipTranslationOptions.RelationshipMinification.NON_MINIMIZED_RELATIONSHIPS
                )
        );
        return classFrameProvider.getFrame(subject, options);
    }

    private Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(OWLNamedIndividual subjectIndividual) {
        return projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> classAssertionsByIndividual.getClassAssertionAxioms(subjectIndividual, ontId));
    }

    private Stream<OWLSameIndividualAxiom> getSameIndividualAxioms(OWLNamedIndividual subjectIndividual) {
        return projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> sameIndividualAxiomsIndex.getSameIndividualAxioms(subjectIndividual, ontId));
    }

    private Stream<PlainPropertyValue> toAssertedPropertyValues(OWLNamedIndividual subjectIndividual,
                                                           OWLAxiom ax) {
        return axiomPropertyValueTranslatorProvider.get()
                .getPropertyValues(subjectIndividual, ax, State.ASSERTED)
                .stream();
    }

    private Set<OWLAxiom> getRelevantAxioms(OWLNamedIndividual subject) {
        var classAssertions = getClassAssertionAxioms(subject);
        var propertyAssertions = getPropertyAssertionAxioms(subject);
        return Streams.concat(classAssertions, propertyAssertions).collect(toImmutableSet());
    }

    private Stream<OWLAxiom> getPropertyAssertionAxioms(OWLNamedIndividual subject) {
        return projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> assertionsBySubject.getPropertyAssertions(subject, ontId));
    }

    @Nonnull
    public Set<OWLAxiom> getAxioms(@Nonnull PlainNamedIndividualFrame frame,
                                   @Nonnull Mode mode) {
        return translateToAxioms(frame.getSubject(), frame, mode);
    }

    private Set<OWLAxiom> translateToAxioms(OWLNamedIndividual subject,
                                            PlainNamedIndividualFrame frame,
                                            Mode mode) {
        var result = new HashSet<OWLAxiom>();
        for(OWLClass cls : frame.getParents()) {
            result.add(dataFactory.getOWLClassAssertionAxiom(cls, subject));
        }
        for(PlainPropertyValue propertyValue : frame.getPropertyValues()) {
            var translator = axiomPropertyValueTranslatorProvider.get();
            result.addAll(translator.getAxioms(subject, propertyValue, mode));
        }
        for(OWLNamedIndividual individual : frame.getSameIndividuals()) {
            result.add(dataFactory.getOWLSameIndividualAxiom(subject, individual));
        }
        return result;
    }
}
