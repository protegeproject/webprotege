package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDatatypeData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class DataPropertyFrameTranslator implements FrameTranslator<DataPropertyFrame, OWLDataPropertyData> {

    @Override
    public DataPropertyFrame getFrame(OWLDataPropertyData subject, OWLOntology rootOntology, Project project) {
        Set<OWLAxiom> propertyValueAxioms = new HashSet<>();
        Set<OWLClassData> domains = new HashSet<>();
        Set<OWLDatatypeData> ranges = new HashSet<>();
        boolean functional = false;
        for(OWLOntology ontology : rootOntology.getImportsClosure()) {
            propertyValueAxioms.addAll(ontology.getAnnotationAssertionAxioms(subject.getEntity().getIRI()));
            for(OWLDataPropertyDomainAxiom ax : ontology.getDataPropertyDomainAxioms(subject.getEntity())) {
                if(!ax.getDomain().isAnonymous()) {
                    domains.add(project.getRenderingManager().getRendering(ax.getDomain().asOWLClass()));
                }
            }
            for(OWLDataPropertyRangeAxiom ax : ontology.getDataPropertyRangeAxioms(subject.getEntity())) {
                if(ax.getRange().isDatatype()) {
                    ranges.add(project.getRenderingManager().getRendering(ax.getRange().asOWLDatatype()));
                }
            }
            if(EntitySearcher.isFunctional(subject.getEntity(), ontology)) {
                functional = true;
            }
        }
        Set<PropertyValue> propertyValues = new HashSet<>();
        AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
        for(OWLAxiom ax : propertyValueAxioms) {
            propertyValues.addAll(translator.getPropertyValues(subject.getEntity(), ax, rootOntology, PropertyValueState.ASSERTED, project.getRenderingManager()));
        }

        PropertyValueList pvl = new PropertyValueList(propertyValues);
        return new DataPropertyFrame(subject, pvl, domains, ranges, functional);
    }

    @Override
    public Set<OWLAxiom> getAxioms(DataPropertyFrame frame, Mode mode) {
        Set<OWLAxiom> result = new HashSet<OWLAxiom>();
        for(PropertyAnnotationValue pv : frame.getAnnotationPropertyValues()) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            result.addAll(translator.getAxioms(frame.getSubject().getEntity(), pv, mode));
        }
        for(OWLClassData domain : frame.getDomains()) {
            OWLAxiom ax = DataFactory.get().getOWLDataPropertyDomainAxiom(frame.getSubject().getEntity(), domain.getEntity());
            result.add(ax);
        }
        for(OWLDatatypeData range : frame.getRanges()) {
            OWLAxiom ax = DataFactory.get().getOWLDataPropertyRangeAxiom(frame.getSubject().getEntity(), range.getEntity());
            result.add(ax);
        }
        if(frame.isFunctional()) {
            result.add(DataFactory.get().getOWLFunctionalDataPropertyAxiom(frame.getSubject().getEntity()));
        }
        return result;
    }
}
