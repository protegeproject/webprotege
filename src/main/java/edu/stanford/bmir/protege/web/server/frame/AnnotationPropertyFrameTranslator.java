package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class AnnotationPropertyFrameTranslator implements FrameTranslator<AnnotationPropertyFrame, OWLAnnotationPropertyData> {

    @Override
    public AnnotationPropertyFrame getFrame(OWLAnnotationPropertyData subject, OWLOntology rootOntology, Project project) {
        Set<PropertyAnnotationValue> propertyValues = new HashSet<>();
        Set<OWLEntityData> domains = new HashSet<>();
        Set<OWLEntityData> ranges = new HashSet<>();
        for(OWLOntology ont : rootOntology.getImportsClosure()) {
            for(OWLAnnotationAssertionAxiom ax : ont.getAnnotationAssertionAxioms(subject.getEntity().getIRI())) {
                if (!(ax.getValue() instanceof OWLAnonymousIndividual)) {
                    propertyValues.add(new PropertyAnnotationValue(project.getRenderingManager().getRendering(ax.getProperty()),
                                                                   project.getRenderingManager().getRendering(ax.getValue()),
                                                                   State.ASSERTED));
                }
            }
            for(OWLAnnotationPropertyDomainAxiom ax : ont.getAnnotationPropertyDomainAxioms(subject.getEntity())) {
                rootOntology.getEntitiesInSignature(ax.getDomain(), Imports.INCLUDED)
                        .stream()
                        .map(e -> project.getRenderingManager().getRendering(e))
                        .forEach(domains::add);
            }
            for(OWLAnnotationPropertyRangeAxiom ax : ont.getAnnotationPropertyRangeAxioms(subject.getEntity())) {
                rootOntology.getEntitiesInSignature(ax.getRange(), Imports.INCLUDED)
                        .stream()
                        .map(e -> project.getRenderingManager().getRendering(e))
                        .forEach(ranges::add);
            }
        }
        return new AnnotationPropertyFrame(project.getRenderingManager().getRendering(subject.getEntity()),
                                           propertyValues, domains, ranges);
    }

    @Override
    public Set<OWLAxiom> getAxioms(AnnotationPropertyFrame frame, Mode mode) {
        Set<OWLAxiom> result = new HashSet<>();
        for(PropertyAnnotationValue value : frame.getPropertyValues()) {
            value.getValue().asAnnotationValue().ifPresent(annotationValue -> {
                result.add(DataFactory.get().getOWLAnnotationAssertionAxiom(value.getProperty().getEntity(),
                                                                            frame.getSubject().getEntity().getIRI(),
                                                                            annotationValue));
            });
        }
        for(OWLEntityData domain : frame.getDomains()) {
            result.add(DataFactory.get().getOWLAnnotationPropertyDomainAxiom(frame.getSubject().getEntity(), domain.getEntity().getIRI()));
        }
        for(OWLEntityData range : frame.getRanges()) {
            result.add(DataFactory.get().getOWLAnnotationPropertyRangeAxiom(frame.getSubject().getEntity(), range.getEntity().getIRI()));
        }
        return result;
    }
}
