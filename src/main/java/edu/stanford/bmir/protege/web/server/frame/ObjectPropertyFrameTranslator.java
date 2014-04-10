package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueState;
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
    public ObjectPropertyFrame getFrame(OWLObjectProperty subject, OWLOntology rootOntology, OWLAPIProject project) {
        Set<OWLAxiom> propertyValueAxioms = new HashSet<OWLAxiom>();

        Set<OWLClass> domains = new HashSet<OWLClass>();
        Set<OWLClass> ranges = new HashSet<OWLClass>();
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
        }
        AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
        Set<PropertyAnnotationValue> propertyValues = new HashSet<PropertyAnnotationValue>();
        for(OWLAxiom ax : propertyValueAxioms) {
            Set<PropertyValue> translationResult = translator.getPropertyValues(subject, ax, rootOntology,
                    PropertyValueState.ASSERTED);
            for(PropertyValue pv : translationResult) {
                if(pv.isAnnotation()) {
                    propertyValues.add((PropertyAnnotationValue) pv);
                }
            }

        }
        return new ObjectPropertyFrame(subject, propertyValues, domains, ranges, Collections.<OWLObjectProperty>emptySet());
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
        return result;
    }
}
