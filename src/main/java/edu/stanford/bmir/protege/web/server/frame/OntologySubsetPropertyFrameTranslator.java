package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/12/2012
 */
public class OntologySubsetPropertyFrameTranslator {

    private final ProjectId projectId;

    public OntologySubsetPropertyFrameTranslator(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ObjectPropertyFrame getFrame(OWLObjectProperty property) {
        ObjectPropertyFrame.Builder builder = new ObjectPropertyFrame.Builder(property);
        AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
        final OWLOntology rootOntology = OWLAPIProjectManager.getProjectManager().getProject(projectId).getRootOntology();
        for(OWLAxiom ax : rootOntology.getAnnotationAssertionAxioms(property.getIRI())) {
            for(PropertyValue propertyValue : translator.getPropertyValues(property, ax, rootOntology)) {
                builder.addPropertyValue((PropertyAnnotationValue) propertyValue);
            }
        }
        return builder.build();
    }
}
