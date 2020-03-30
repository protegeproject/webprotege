package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByIndividualIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.PropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.SameIndividualAxiomsIndex;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
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
public class NamedIndividualFrameTranslator implements EntityFrameTranslator<NamedIndividualFrame, OWLNamedIndividualData> {


    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ClassAssertionAxiomsByIndividualIndex classAssertionsByIndividual;

    @Nonnull
    private final PropertyAssertionAxiomsBySubjectIndex assertionsBySubject;

    @Nonnull
    private final SameIndividualAxiomsIndex sameIndividualAxiomsIndex;

    @Nonnull
    private final ContextRenderer renderer;

    @Nonnull
    private final PropertyValueMinimiser propertyValueMinimiser;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Nonnull
    private final Provider<ClassFrameTranslator> translatorProvider;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public NamedIndividualFrameTranslator(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                          @Nonnull ClassAssertionAxiomsByIndividualIndex classAssertionsByIndividual,
                                          @Nonnull PropertyAssertionAxiomsBySubjectIndex assertionsBySubject,
                                          @Nonnull SameIndividualAxiomsIndex sameIndividualAxiomsIndex,
                                          @Nonnull ContextRenderer renderer,
                                          @Nonnull PropertyValueMinimiser propertyValueMinimiser,
                                          @Nonnull PropertyValueComparator propertyValueComparator,
                                          @Nonnull Provider<ClassFrameTranslator> translatorProvider,
                                          @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider,
                                          @Nonnull OWLDataFactory dataFactory) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.classAssertionsByIndividual = checkNotNull(classAssertionsByIndividual);
        this.assertionsBySubject = checkNotNull(assertionsBySubject);
        this.sameIndividualAxiomsIndex = checkNotNull(sameIndividualAxiomsIndex);
        this.renderer = checkNotNull(renderer);
        this.propertyValueMinimiser = checkNotNull(propertyValueMinimiser);
        this.propertyValueComparator = checkNotNull(propertyValueComparator);
        this.translatorProvider = checkNotNull(translatorProvider);
        this.axiomPropertyValueTranslatorProvider = checkNotNull(axiomPropertyValueTranslatorProvider);
        this.dataFactory = checkNotNull(dataFactory);
    }

    /**
     * Gets the entity type that this translator translates.
     *
     * @return The entity type.  Not {@code null}.
     */
    @Override
    public EntityType<OWLNamedIndividual> getEntityType() {
        return EntityType.NAMED_INDIVIDUAL;
    }

    @Nonnull
    @Override
    public NamedIndividualFrame getFrame(@Nonnull OWLNamedIndividualData subject) {
        return translateToNamedIndividualFrame(subject, true);
    }

    @Nonnull
    public NamedIndividualFrame getFrame(@Nonnull OWLNamedIndividualData subject,
                                         boolean includeDerivedInformation) {
        return translateToNamedIndividualFrame(subject, false);
    }

    private NamedIndividualFrame translateToNamedIndividualFrame(OWLNamedIndividualData subject,
                                                                 boolean includeDerived) {
        var subjectIndividual = subject.getEntity();
        var relevantAxioms = getRelevantAxioms(subjectIndividual);

        var types = relevantAxioms
                .stream()
                .filter(OWLClassAssertionAxiom.class::isInstance)
                .map(OWLClassAssertionAxiom.class::cast)
                .map(OWLClassAssertionAxiom::getClassExpression)
                .filter(OWLClassExpression::isNamed)
                .map(OWLClassExpression::asOWLClass)
                .map(toClassData())
                .sorted()
                .collect(toImmutableSet());

        var assertedPropertyValues =
                relevantAxioms.stream()
                .flatMap(ax -> toAssertedPropertyValues(subjectIndividual, ax));

        Stream<PropertyValue> derivedPropertyValues;

        if(includeDerived) {
            derivedPropertyValues = getClassAssertionAxioms(subjectIndividual)
                    .map(OWLClassAssertionAxiom::getClassExpression)
                    .filter(OWLClassExpression::isNamed)
                    .map(OWLClassExpression::asOWLClass)
                    .map(toClassData())
                    .map(clsData -> translatorProvider.get().getFrame(clsData))
                    .flatMap(translator -> translator.getPropertyValues().stream())
                    .filter(PropertyValue::isLogical)
                    .map(propertyValue -> propertyValue.setState(State.DERIVED));
        }
        else {
            derivedPropertyValues = Stream.empty();
        }

        var propertyValues =
                Streams.concat(assertedPropertyValues, derivedPropertyValues)
                .collect(toImmutableList());

        var propertyValuesMin = propertyValueMinimiser.minimisePropertyValues(propertyValues)
                .sorted(propertyValueComparator)
                .collect(toImmutableSet());

        var sameIndividuals = getSameIndividualAxioms(subjectIndividual)
                .flatMap(ax -> ax.getIndividuals().stream())
                .filter(OWLIndividual::isNamed)
                .filter(ind -> !ind.equals(subjectIndividual))
                .map(OWLIndividual::asOWLNamedIndividual)
                .map(renderer::getIndividualData)
                .sorted()
                .collect(toImmutableSet());

        return NamedIndividualFrame.get(subject, types, propertyValuesMin, sameIndividuals);
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

    private Stream<PropertyValue> toAssertedPropertyValues(OWLNamedIndividual subjectIndividual,
                                                           OWLAxiom ax) {
        return axiomPropertyValueTranslatorProvider.get()
                .getPropertyValues(subjectIndividual, ax, State.ASSERTED)
                .stream();
    }

    private Function<OWLClass, OWLClassData> toClassData() {
        return renderer::getClassData;
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
    @Override
    public Set<OWLAxiom> getAxioms(@Nonnull NamedIndividualFrame frame,
                                   @Nonnull Mode mode) {
        return translateToAxioms(frame.getSubject().getEntity(), frame, mode);
    }

    private Set<OWLAxiom> translateToAxioms(OWLNamedIndividual subject,
                                            NamedIndividualFrame frame,
                                            Mode mode) {
        var result = new HashSet<OWLAxiom>();
        for(OWLClassData cls : frame.getClasses()) {
            result.add(dataFactory.getOWLClassAssertionAxiom(cls.getEntity(), subject));
        }
        for(PropertyValue propertyValue : frame.getPropertyValues()) {
            var translator = axiomPropertyValueTranslatorProvider.get();
            result.addAll(translator.getAxioms(subject, propertyValue, mode));
        }
        for(OWLNamedIndividualData individual : frame.getSameIndividuals()) {
            result.add(dataFactory.getOWLSameIndividualAxiom(subject, individual.getEntity()));
        }
        return result;
    }
}
