package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueState;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class AnnotationPropertyFrameTranslator implements FrameTranslator<AnnotationPropertyFrame, OWLAnnotationProperty> {

    @Override
    public AnnotationPropertyFrame getFrame(OWLAnnotationProperty subject, OWLOntology rootOntology, Project project) {
        Set<PropertyAnnotationValue> propertyValues = new HashSet<PropertyAnnotationValue>();
        Set<OWLEntity> domains = new HashSet<OWLEntity>();
        Set<OWLEntity> ranges = new HashSet<OWLEntity>();
        for(OWLOntology ont : rootOntology.getImportsClosure()) {
            for(OWLAnnotationAssertionAxiom ax : ont.getAnnotationAssertionAxioms(subject.getIRI())) {
                propertyValues.add(new PropertyAnnotationValue(ax.getProperty(), ax.getValue(), PropertyValueState.ASSERTED));
            }
            for(OWLAnnotationPropertyDomainAxiom ax : ont.getAnnotationPropertyDomainAxioms(subject)) {
                domains.addAll(rootOntology.getEntitiesInSignature(ax.getDomain()));
            }
            for(OWLAnnotationPropertyRangeAxiom ax : ont.getAnnotationPropertyRangeAxioms(subject)) {
                ranges.addAll(rootOntology.getEntitiesInSignature(ax.getRange()));
            }
        }
        return new AnnotationPropertyFrame(subject, propertyValues, domains, ranges);
    }

    @Override
    public Set<OWLAxiom> getAxioms(AnnotationPropertyFrame frame, Mode mode) {
        Set<OWLAxiom> result = new HashSet<OWLAxiom>();
        for(PropertyAnnotationValue value : frame.getPropertyValues()) {
            result.add(DataFactory.get().getOWLAnnotationAssertionAxiom(value.getProperty(), frame.getSubject().getIRI(), value.getValue()));
        }
        for(OWLEntity domain : frame.getDomains()) {
            result.add(DataFactory.get().getOWLAnnotationPropertyDomainAxiom(frame.getSubject(), domain.getIRI()));
        }
        for(OWLEntity range : frame.getRanges()) {
            result.add(DataFactory.get().getOWLAnnotationPropertyRangeAxiom(frame.getSubject(), range.getIRI()));
        }
        return result;
    }
}
