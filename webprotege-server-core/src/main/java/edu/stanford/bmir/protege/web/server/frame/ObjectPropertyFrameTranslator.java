package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
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
    private final OWLOntology rootOntology;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Inject
    public ObjectPropertyFrameTranslator(@Nonnull ContextRenderer ren,
                                         @Nonnull @RootOntology OWLOntology rootOntology,
                                         @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider, @Nonnull PropertyValueComparator propertyValueComparator) {
        this.ren = ren;
        this.rootOntology = rootOntology;
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
        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            propertyValueAxioms.addAll(ontology.getAnnotationAssertionAxioms(subject.getEntity().getIRI()));
            for (OWLObjectPropertyDomainAxiom ax : ontology.getObjectPropertyDomainAxioms(subject.getEntity())) {
                final OWLClassExpression domain = ax.getDomain();
                if (!domain.isAnonymous()) {
                    domains.add(ren.getClassData(domain.asOWLClass()));
                }
            }
            for (OWLObjectPropertyRangeAxiom ax : ontology.getObjectPropertyRangeAxioms(subject.getEntity())) {
                OWLClassExpression range = ax.getRange();
                if (!range.isAnonymous()) {
                    ranges.add(ren.getClassData(range.asOWLClass()));
                }
            }
            if (ontology.getAxiomCount(AxiomType.FUNCTIONAL_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.FUNCTIONAL);
            }
            if (ontology.getAxiomCount(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.INVERSE_FUNCTIONAL);
            }
            if (ontology.getAxiomCount(AxiomType.SYMMETRIC_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.SYMMETRIC);
            }
            if (ontology.getAxiomCount(AxiomType.ASYMMETRIC_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.ASYMMETRIC);
            }
            if (ontology.getAxiomCount(AxiomType.REFLEXIVE_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.REFLEXIVE);
            }
            if (ontology.getAxiomCount(AxiomType.IRREFLEXIVE_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.IRREFLEXIVE);
            }
            if (ontology.getAxiomCount(AxiomType.TRANSITIVE_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.TRANSITIVE);
            }
        }
        AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
        ImmutableSet<PropertyAnnotationValue> propertyValues = propertyValueAxioms.stream()
                                                                                   .flatMap(ax -> translator.getPropertyValues(subject.getEntity(), ax, rootOntology, State.ASSERTED).stream())
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
