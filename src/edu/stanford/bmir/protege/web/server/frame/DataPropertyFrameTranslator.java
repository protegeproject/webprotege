package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.DataPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueList;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class DataPropertyFrameTranslator implements FrameTranslator<DataPropertyFrame, OWLDataProperty> {

    @Override
    public DataPropertyFrame getFrame(OWLDataProperty subject, OWLOntology rootOntology, OWLAPIProject project) {
        Set<OWLAxiom> propertyValueAxioms = new HashSet<OWLAxiom>();
        Set<OWLClass> domains = new HashSet<OWLClass>();
        Set<OWLDatatype> ranges = new HashSet<OWLDatatype>();
        boolean functional = false;
        for(OWLOntology ontology : rootOntology.getImportsClosure()) {
            propertyValueAxioms.addAll(ontology.getAnnotationAssertionAxioms(subject.getIRI()));
            for(OWLDataPropertyDomainAxiom ax : ontology.getDataPropertyDomainAxioms(subject)) {
                if(!ax.getDomain().isAnonymous()) {
                    domains.add(ax.getDomain().asOWLClass());
                }
            }
            for(OWLDataPropertyRangeAxiom ax : ontology.getDataPropertyRangeAxioms(subject)) {
                if(ax.getRange().isDatatype()) {
                    ranges.add(ax.getRange().asOWLDatatype());
                }
            }
            if(subject.isFunctional(ontology)) {
                functional = true;
            }
        }
        Set<PropertyValue> propertyValues = new HashSet<PropertyValue>();
        AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
        for(OWLAxiom ax : propertyValueAxioms) {
            propertyValues.addAll(translator.getPropertyValues(subject, ax, rootOntology));
        }

        PropertyValueList pvl = new PropertyValueList(propertyValues);
        return new DataPropertyFrame(subject, pvl, domains, ranges, functional);
    }

    @Override
    public Set<OWLAxiom> getAxioms(DataPropertyFrame frame) {
        Set<OWLAxiom> result = new HashSet<OWLAxiom>();
        for(PropertyAnnotationValue pv : frame.getAnnotationPropertyValues()) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            result.addAll(translator.getAxioms(frame.getSubject(), pv));
        }
        for(OWLClass domain : frame.getDomains()) {
            OWLAxiom ax = DataFactory.get().getOWLDataPropertyDomainAxiom(frame.getSubject(), domain);
            result.add(ax);
        }
        for(OWLDatatype range : frame.getRanges()) {
            OWLAxiom ax = DataFactory.get().getOWLDataPropertyRangeAxiom(frame.getSubject(), range);
            result.add(ax);
        }
        if(frame.isFunctional()) {
            result.add(DataFactory.get().getOWLFunctionalDataPropertyAxiom(frame.getSubject()));
        }
        return result;
    }
}
