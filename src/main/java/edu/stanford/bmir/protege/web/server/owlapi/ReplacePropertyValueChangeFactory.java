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
 * Date: 11/04/2012
 */
public class ReplacePropertyValueChangeFactory extends AbstractPropertyValueChangeFactory {

    private EntityData oldEntityData;

    public ReplacePropertyValueChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String entityName, PropertyEntityData propertyEntityData, EntityData oldEntityData, EntityData entityData) {
        super(project, userId, changeDescription, entityName, propertyEntityData, entityData);
        this.oldEntityData = oldEntityData;
    }

    @Override
    protected void createChanges(OWLAnnotationSubject subject, OWLAnnotationProperty property, OWLAnnotationValue value, List<OWLOntologyChange> changeListToFill) {
        OWLAnnotationValue oldAnnotationValue = getAnnotationValue(oldEntityData);
        OWLAxiom axiomToRemove = getDataFactory().getOWLAnnotationAssertionAxiom(property, subject, oldAnnotationValue);
        OWLAxiom axiomToAdd = getDataFactory().getOWLAnnotationAssertionAxiom(property, subject, value, axiomToRemove.getAnnotations());
        createChanges(changeListToFill, axiomToRemove, axiomToAdd);
    }

    @Override
    protected void createChanges(OWLIndividual subject, OWLObjectProperty property, OWLIndividual value, List<OWLOntologyChange> changeListToFill) {
        String name = oldEntityData.getName() == null ? oldEntityData.getBrowserText() : oldEntityData.getName();
        OWLIndividual oldValue = getRenderingManager().getEntity(name, EntityType.NAMED_INDIVIDUAL);
        OWLAxiom axiomToRemove = getDataFactory().getOWLObjectPropertyAssertionAxiom(property, subject, oldValue);
        OWLAxiom axiomToAdd = getDataFactory().getOWLObjectPropertyAssertionAxiom(property, subject, value);
        createChanges(changeListToFill, axiomToRemove, axiomToAdd);
    }

    @Override
    protected void createChanges(OWLIndividual subject, OWLDataProperty property, OWLLiteral value, List<OWLOntologyChange> changeListToFill) {
        OWLLiteral oldValue = getRenderingManager().getLiteral(oldEntityData);
        OWLAxiom axiomToRemove = getDataFactory().getOWLDataPropertyAssertionAxiom(property, subject, oldValue);
        OWLAxiom axiomToAdd = getDataFactory().getOWLDataPropertyAssertionAxiom(property, subject, value);
        createChanges(changeListToFill, axiomToRemove, axiomToAdd);
    }


    private void createChanges(List<OWLOntologyChange> changeListToFill, OWLAxiom axiomToRemove, OWLAxiom axiomToAdd) {
        int changes = 0;
        for(OWLOntology ontology : getRootOntology().getImportsClosure()) {
            for(OWLAxiom possiblyAnnotated : ontology.getAxiomsIgnoreAnnotations(axiomToRemove)) {
                changeListToFill.add(new RemoveAxiom(ontology, possiblyAnnotated));
                OWLAxiom annotatedAxiomToAdd = axiomToAdd.getAnnotatedAxiom(possiblyAnnotated.getAnnotations());
                changeListToFill.add(new AddAxiom(ontology, annotatedAxiomToAdd));
                changes++;
            }
        }
//        if(changes == 0) {
//            changeListToFill.add(new AddAxiom(getRootOntology(), axiomToAdd));
//        }
    }
}
