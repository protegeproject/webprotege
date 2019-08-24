package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
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
public class ObjectPropertyFrameTranslator implements FrameTranslator<ObjectPropertyFrame, OWLObjectPropertyData> {

    @Nonnull
    private final ContextRenderer ren;

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
    private final PropertyValueComparator propertyValueComparator;

    @Inject
    public ObjectPropertyFrameTranslator(@Nonnull ContextRenderer ren,
                                         @Nonnull ProjectOntologiesIndex ontologiesIndex,
                                         @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubject,
                                         @Nonnull ObjectPropertyDomainAxiomsIndex objectPropertyDomainAxiomsIndex,
                                         @Nonnull ObjectPropertyRangeAxiomsIndex objectPropertyRangeAxiomsIndex,
                                         @Nonnull ObjectPropertyCharacteristicsIndex objectPropertyCharacteristicsIndex,
                                         @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider,
                                         @Nonnull PropertyValueComparator propertyValueComparator) {
        this.ren = ren;
        this.ontologiesIndex = ontologiesIndex;
        this.annotationAssertionAxiomsBySubject = annotationAssertionAxiomsBySubject;
        this.objectPropertyDomainAxiomsIndex = objectPropertyDomainAxiomsIndex;
        this.objectPropertyRangeAxiomsIndex = objectPropertyRangeAxiomsIndex;
        this.objectPropertyCharacteristicsIndex = objectPropertyCharacteristicsIndex;
        this.axiomPropertyValueTranslatorProvider = axiomPropertyValueTranslatorProvider;
        this.propertyValueComparator = propertyValueComparator;
    }

    @Nonnull
    @Override
    public ObjectPropertyFrame getFrame(@Nonnull OWLObjectPropertyData subject) {
        Set<OWLAxiom> propertyValueAxioms = new HashSet<>();
        ImmutableSet.Builder<OWLClassData> domains = ImmutableSet.builder();
        ImmutableSet.Builder<OWLClassData> ranges = ImmutableSet.builder();
        ImmutableSet.Builder<ObjectPropertyCharacteristic> characteristics = ImmutableSet.builder();

        var subjectEntity = subject.getEntity();
        ontologiesIndex.getOntologyIds()
                .forEach(ontologyId -> {
                    annotationAssertionAxiomsBySubject.getAxiomsForSubject(subjectEntity.getIRI(), ontologyId)
                            .forEach(propertyValueAxioms::add);

                    objectPropertyDomainAxiomsIndex.getObjectPropertyDomainAxioms(subjectEntity, ontologyId)
                            .map(OWLPropertyDomainAxiom::getDomain)
                            .filter(OWLClassExpression::isNamed)
                            .map(OWLClassExpression::asOWLClass)
                            .map(ren::getClassData)
                            .forEach(domains::add);

                    objectPropertyRangeAxiomsIndex.getObjectPropertyRangeAxioms(subjectEntity, ontologyId)
                            .map(OWLPropertyRangeAxiom::getRange)
                            .filter(OWLClassExpression::isNamed)
                            .map(OWLClassExpression::asOWLClass)
                            .map(ren::getClassData)
                            .forEach(ranges::add);

                    Stream.of(ObjectPropertyCharacteristic.values())
                            .filter(characteristic -> objectPropertyCharacteristicsIndex.hasCharacteristic(subjectEntity, characteristic, ontologyId))
                            .forEach(characteristics::add);
                });

        AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
        ImmutableSet<PropertyAnnotationValue> propertyValues = propertyValueAxioms.stream()
                                                                                   .flatMap(ax -> translator.getPropertyValues(subjectEntity, ax, State.ASSERTED).stream())
                                                                                   .filter(PropertyValue::isAnnotation)
                                                                                   .map(pv -> (PropertyAnnotationValue) pv)
                                                                                   .distinct()
                                                                                   .sorted(propertyValueComparator)
                                                                                   .collect(toImmutableSet());
        return ObjectPropertyFrame.get(subject,
                                       propertyValues,
                                       domains.build(),
                                       ranges.build(),
                                       ImmutableSet.of(),
                                       characteristics.build());
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxioms(@Nonnull ObjectPropertyFrame frame, @Nonnull Mode mode) {
        Set<OWLAxiom> result = new HashSet<>();
        for (PropertyAnnotationValue pv : frame.getAnnotationPropertyValues()) {
            AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
            result.addAll(translator.getAxioms(frame.getSubject().getEntity(), pv, mode));
        }
        for (OWLClassData domain : frame.getDomains()) {
            OWLAxiom ax = DataFactory.get().getOWLObjectPropertyDomainAxiom(frame.getSubject().getEntity(), domain.getEntity());
            result.add(ax);
        }
        for (OWLClassData range : frame.getRanges()) {
            OWLAxiom ax = DataFactory.get().getOWLObjectPropertyRangeAxiom(frame.getSubject().getEntity(), range.getEntity());
            result.add(ax);
        }
        for (ObjectPropertyCharacteristic characteristic : frame.getCharacteristics()) {
            OWLAxiom ax = characteristic.createAxiom(frame.getSubject().getEntity(), DataFactory.get());
            result.add(ax);
        }
        return result;
    }
}
