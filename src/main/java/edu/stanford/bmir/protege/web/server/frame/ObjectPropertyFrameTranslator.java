package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class ObjectPropertyFrameTranslator implements FrameTranslator<ObjectPropertyFrame, OWLObjectProperty> {

    @Override
    public ObjectPropertyFrame getFrame(OWLObjectProperty subject, OWLOntology rootOntology, Project project) {
        Set<OWLAxiom> propertyValueAxioms = new HashSet<OWLAxiom>();

        Set<OWLClass> domains = new HashSet<>();
        Set<OWLClass> ranges = new HashSet<>();
        Set<ObjectPropertyCharacteristic> characteristics = new HashSet<>();
        for(OWLOntology ontology : rootOntology.getImportsClosure()) {
            propertyValueAxioms.addAll(ontology.getAnnotationAssertionAxioms(subject.getIRI()));
            for(OWLObjectPropertyDomainAxiom ax : ontology.getObjectPropertyDomainAxioms(subject)) {
                final OWLClassExpression domain = ax.getDomain();
                if (!domain.isAnonymous()) {
                    domains.add(domain.asOWLClass());
                }
            }
            for(OWLObjectPropertyRangeAxiom ax : ontology.getObjectPropertyRangeAxioms(subject)) {
                OWLClassExpression range = ax.getRange();
                if(!range.isAnonymous()) {
                    ranges.add(range.asOWLClass());
                }
            }
            if(ontology.getAxiomCount(AxiomType.FUNCTIONAL_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.FUNCTIONAL);
            }
            if(ontology.getAxiomCount(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.INVERSE_FUNCTIONAL);
            }
            if(ontology.getAxiomCount(AxiomType.SYMMETRIC_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.SYMMETRIC);
            }
            if(ontology.getAxiomCount(AxiomType.ASYMMETRIC_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.ASYMMETRIC);
            }
            if(ontology.getAxiomCount(AxiomType.REFLEXIVE_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.REFLEXIVE);
            }
            if(ontology.getAxiomCount(AxiomType.IRREFLEXIVE_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.IRREFLEXIVE);
            }
            if(ontology.getAxiomCount(AxiomType.TRANSITIVE_OBJECT_PROPERTY) > 1) {
                characteristics.add(ObjectPropertyCharacteristic.TRANSITIVE);
            }
        }
        AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
        Set<PropertyAnnotationValue> propertyValues = new HashSet<>();
        for(OWLAxiom ax : propertyValueAxioms) {
            Set<PropertyValue> translationResult = translator.getPropertyValues(subject, ax, rootOntology,
                    PropertyValueState.ASSERTED);
            for(PropertyValue pv : translationResult) {
                if(pv.isAnnotation()) {
                    propertyValues.add((PropertyAnnotationValue) pv);
                }
            }

        }
        return new ObjectPropertyFrame(subject, propertyValues, domains, ranges, Collections.emptySet(), characteristics);
    }

    @Override
    public Set<OWLAxiom> getAxioms(ObjectPropertyFrame frame, Mode mode) {
        Set<OWLAxiom> result = new HashSet<OWLAxiom>();
        for(PropertyAnnotationValue pv : frame.getAnnotationPropertyValues()) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            result.addAll(translator.getAxioms(frame.getSubject(), pv, mode));
        }
        for(OWLClass domain : frame.getDomains()) {
            OWLAxiom ax = DataFactory.get().getOWLObjectPropertyDomainAxiom(frame.getSubject(), domain);
            result.add(ax);
        }
        for(OWLClass range : frame.getRanges()) {
            OWLAxiom ax = DataFactory.get().getOWLObjectPropertyRangeAxiom(frame.getSubject(), range);
            result.add(ax);
        }
        for(ObjectPropertyCharacteristic characteristic : frame.getCharacteristics()) {
            OWLAxiom ax = characteristic.createAxiom(frame.getSubject(), DataFactory.get());
            result.add(ax);
        }
        return result;
    }
}
