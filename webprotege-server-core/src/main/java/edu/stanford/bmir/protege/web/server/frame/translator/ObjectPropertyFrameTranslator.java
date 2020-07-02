package edu.stanford.bmir.protege.web.server.frame.translator;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.frame.Mode;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class ObjectPropertyFrameTranslator {

    @Nonnull
    private final ProjectOntologiesIndex ontologiesIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubject;

    @Nonnull
    private final ObjectPropertyDomainAxiomsIndex objectPropertyDomainAxiomsIndex;

    @Nonnull
    private final ObjectPropertyRangeAxiomsIndex objectPropertyRangeAxiomsIndex;

    @Nonnull
    private final ObjectPropertyCharacteristicsIndex objectPropertyCharacteristicsIndex;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    @Nonnull
    private final PropertyValue2AxiomTranslator propertyValue2AxiomTranslator;

    @Inject
    public ObjectPropertyFrameTranslator(@Nonnull ProjectOntologiesIndex ontologiesIndex,
                                         @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubject,
                                         @Nonnull ObjectPropertyDomainAxiomsIndex objectPropertyDomainAxiomsIndex,
                                         @Nonnull ObjectPropertyRangeAxiomsIndex objectPropertyRangeAxiomsIndex,
                                         @Nonnull ObjectPropertyCharacteristicsIndex objectPropertyCharacteristicsIndex,
                                         @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider,
                                         @Nonnull PropertyValue2AxiomTranslator propertyValue2AxiomTranslator) {
        this.ontologiesIndex = ontologiesIndex;
        this.annotationAssertionAxiomsBySubject = annotationAssertionAxiomsBySubject;
        this.objectPropertyDomainAxiomsIndex = objectPropertyDomainAxiomsIndex;
        this.objectPropertyRangeAxiomsIndex = objectPropertyRangeAxiomsIndex;
        this.objectPropertyCharacteristicsIndex = objectPropertyCharacteristicsIndex;
        this.axiomPropertyValueTranslatorProvider = axiomPropertyValueTranslatorProvider;
        this.propertyValue2AxiomTranslator = propertyValue2AxiomTranslator;
    }

    @Nonnull
    public PlainObjectPropertyFrame getFrame(@Nonnull OWLObjectProperty subject) {
        Set<OWLAxiom> propertyValueAxioms = new HashSet<>();
        ImmutableSet.Builder<OWLClass> domains = ImmutableSet.builder();
        ImmutableSet.Builder<OWLClass> ranges = ImmutableSet.builder();
        ImmutableSet.Builder<ObjectPropertyCharacteristic> characteristics = ImmutableSet.builder();

        ontologiesIndex.getOntologyIds()
                .forEach(ontologyId -> {
                    annotationAssertionAxiomsBySubject.getAxiomsForSubject(subject.getIRI(), ontologyId)
                            .forEach(propertyValueAxioms::add);

                    objectPropertyDomainAxiomsIndex.getObjectPropertyDomainAxioms(subject, ontologyId)
                            .map(OWLPropertyDomainAxiom::getDomain)
                            .filter(OWLClassExpression::isNamed)
                            .map(OWLClassExpression::asOWLClass)
                            .forEach(domains::add);

                    objectPropertyRangeAxiomsIndex.getObjectPropertyRangeAxioms(subject, ontologyId)
                            .map(OWLPropertyRangeAxiom::getRange)
                            .filter(OWLClassExpression::isNamed)
                            .map(OWLClassExpression::asOWLClass)
                            .forEach(ranges::add);

                    Stream.of(ObjectPropertyCharacteristic.values())
                            .filter(characteristic -> objectPropertyCharacteristicsIndex.hasCharacteristic(subject, characteristic, ontologyId))
                            .forEach(characteristics::add);
                });

        AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
        var propertyValues = propertyValueAxioms.stream()
                                                                                   .flatMap(ax -> translator.getPropertyValues(subject, ax, State.ASSERTED).stream())
                                                                                   .filter(PlainPropertyValue::isAnnotation)
                                                                                   .map(pv -> (PlainPropertyAnnotationValue) pv)
                                                                                   .distinct()
                                                                                   .collect(toImmutableSet());
        return PlainObjectPropertyFrame.get(subject,
                                       propertyValues,
                                            characteristics.build(),
                                       domains.build(),
                                       ranges.build(),
                                       ImmutableSet.of());
    }

    @Nonnull
    public Set<OWLAxiom> getAxioms(@Nonnull PlainObjectPropertyFrame frame, @Nonnull Mode mode) {
        Set<OWLAxiom> result = new HashSet<>();
        for (PlainPropertyAnnotationValue pv : frame.getPropertyValues()) {
            AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
            result.addAll(propertyValue2AxiomTranslator.getAxioms(frame.getSubject(), pv, mode));
        }
        for (OWLClass domain : frame.getDomains()) {
            OWLAxiom ax = DataFactory.get().getOWLObjectPropertyDomainAxiom(frame.getSubject(), domain);
            result.add(ax);
        }
        for (OWLClass range : frame.getRanges()) {
            OWLAxiom ax = DataFactory.get().getOWLObjectPropertyRangeAxiom(frame.getSubject(), range);
            result.add(ax);
        }
        for (ObjectPropertyCharacteristic characteristic : frame.getCharacteristics()) {
            OWLAxiom ax = characteristic.createAxiom(frame.getSubject(), DataFactory.get());
            result.add(ax);
        }
        return result;
    }
}
