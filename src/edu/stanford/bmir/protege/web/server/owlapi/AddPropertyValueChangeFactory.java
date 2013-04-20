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
 * Date: 04/04/2012
 */
public class AddPropertyValueChangeFactory extends AbstractPropertyValueChangeFactory {

    public AddPropertyValueChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String entityName, PropertyEntityData propertyEntityData, EntityData entityData) {
        super(project, userId, changeDescription, entityName, propertyEntityData, entityData);
    }

    @Override
    protected void createChanges(OWLAnnotationSubject subject, OWLAnnotationProperty property, OWLAnnotationValue value, List<OWLOntologyChange> changeListToFill) {
        OWLAxiom axiomToAdd = getDataFactory().getOWLAnnotationAssertionAxiom(property, subject, value);
        changeListToFill.add(new AddAxiom(getRootOntology(), axiomToAdd));
    }

    @Override
    protected void createChanges(OWLIndividual subject, OWLObjectProperty property, OWLIndividual value, List<OWLOntologyChange> changeListToFill) {
        OWLAxiom axiomToAdd = getDataFactory().getOWLObjectPropertyAssertionAxiom(property, subject, value);
        changeListToFill.add(new AddAxiom(getRootOntology(), axiomToAdd));
    }

    @Override
    protected void createChanges(OWLIndividual subject, OWLDataProperty property, OWLLiteral value, List<OWLOntologyChange> changeListToFill) {
        OWLAxiom axiomToAdd = getDataFactory().getOWLDataPropertyAssertionAxiom(property, subject, value);
        changeListToFill.add(new AddAxiom(getRootOntology(), axiomToAdd));
    }
}
