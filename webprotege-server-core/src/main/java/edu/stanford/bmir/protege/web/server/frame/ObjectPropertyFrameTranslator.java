package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class ObjectPropertyFrameTranslator implements FrameTranslator<ObjectPropertyFrame, OWLObjectPropertyData> {

    @Nonnull
    private final RenderingManager rm;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    @Inject
    public ObjectPropertyFrameTranslator(@Nonnull RenderingManager rm,
                                         @Nonnull @RootOntology OWLOntology rootOntology,
                                         @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider) {
        this.rm = rm;
        this.rootOntology = rootOntology;
        this.axiomPropertyValueTranslatorProvider = axiomPropertyValueTranslatorProvider;
    }

    @Override
    public ObjectPropertyFrame getFrame(OWLObjectPropertyData subject) {
        Set<OWLAxiom> propertyValueAxioms = new HashSet<>();
        ImmutableSet.Builder<OWLClassData> domains = ImmutableSet.builder();
        ImmutableSet.Builder<OWLClassData> ranges = ImmutableSet.builder();
        ImmutableList.Builder<ObjectPropertyCharacteristic> characteristics = ImmutableList.builder();
        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            propertyValueAxioms.addAll(ontology.getAnnotationAssertionAxioms(subject.getEntity().getIRI()));
            for (OWLObjectPropertyDomainAxiom ax : ontology.getObjectPropertyDomainAxioms(subject.getEntity())) {
                final OWLClassExpression domain = ax.getDomain();
                if (!domain.isAnonymous()) {
                    domains.add(rm.getRendering(domain.asOWLClass()));
                }
            }
            for (OWLObjectPropertyRangeAxiom ax : ontology.getObjectPropertyRangeAxioms(subject.getEntity())) {
                OWLClassExpression range = ax.getRange();
                if (!range.isAnonymous()) {
                    ranges.add(rm.getRendering(range.asOWLClass()));
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
                                                                                   .sorted()
                                                                                   .collect(toImmutableSet());
        return ObjectPropertyFrame.get(subject,
                                       propertyValues,
                                       domains.build(),
                                       ranges.build(),
                                       ImmutableSet.of(),
                                       characteristics.build());
    }

    @Override
    public Set<OWLAxiom> getAxioms(ObjectPropertyFrame frame, Mode mode) {
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
