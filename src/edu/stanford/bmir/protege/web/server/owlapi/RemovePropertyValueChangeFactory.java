package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2012
 */
public class RemovePropertyValueChangeFactory extends AbstractPropertyValueChangeFactory {

    public RemovePropertyValueChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String entityName, PropertyEntityData propertyEntityData, EntityData entityData) {
        super(project, userId, changeDescription, entityName, propertyEntityData, entityData);
    }

    @Override
    protected void createChanges(OWLIndividual subject, OWLObjectProperty property, OWLIndividual value, List<OWLOntologyChange> changeListToFill) {
        OWLAxiom axiomToRemove = getDataFactory().getOWLObjectPropertyAssertionAxiom(property, subject, value);
        createChanges(changeListToFill, axiomToRemove);
    }

    @Override
    protected void createChanges(OWLIndividual subject, OWLDataProperty property, OWLLiteral value, List<OWLOntologyChange> changeListToFill) {
        OWLAxiom axiomToRemove = getDataFactory().getOWLDataPropertyAssertionAxiom(property, subject, value);
        createChanges(changeListToFill, axiomToRemove);
    }

    @Override
    protected void createChanges(OWLAnnotationSubject subject, OWLAnnotationProperty property, OWLAnnotationValue value, List<OWLOntologyChange> changeListToFill) {
        OWLAxiom axiomToRemove = getDataFactory().getOWLAnnotationAssertionAxiom(property, subject, value);
        createChanges(changeListToFill, axiomToRemove);
    }

    private void createChanges(List<OWLOntologyChange> changeListToFill, OWLAxiom axiomToRemove) {
        for(OWLOntology ontology : getRootOntology().getImportsClosure()) {
            if (ontology.containsAxiomIgnoreAnnotations(axiomToRemove)) {
                for(OWLAxiom ax : ontology.getAxiomsIgnoreAnnotations(axiomToRemove)) {
                    changeListToFill.add(new RemoveAxiom(ontology, ax));
                }
            }
        }
    }
}
