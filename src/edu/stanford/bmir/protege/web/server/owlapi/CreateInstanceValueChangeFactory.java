package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import org.semanticweb.owlapi.model.*;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2012
 */
public class CreateInstanceValueChangeFactory extends OWLOntologyChangeFactory {

    private String instName;
    
    private String typeName;
    
    private String subjectEntity;
    
    String propertyEntity;

    public CreateInstanceValueChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String instName, String typeName, String subjectEntity, String propertyEntity) {
        super(project, userId, changeDescription);
        this.instName = instName;
        this.typeName = typeName;
        this.subjectEntity = subjectEntity;
        this.propertyEntity = propertyEntity;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        RenderingManager rm = getRenderingManager();
        OWLNamedIndividual namedIndividual = rm.getEntity(instName, EntityType.NAMED_INDIVIDUAL);
        OWLClass type = rm.getEntity(typeName, EntityType.CLASS);
        OWLNamedIndividual subject = rm.getEntity(subjectEntity, EntityType.NAMED_INDIVIDUAL);
        OWLObjectProperty property = rm.getEntity(propertyEntity, EntityType.OBJECT_PROPERTY);
        OWLDataFactory df = getDataFactory();
        OWLObjectPropertyAssertionAxiom propertyAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(property, subject, namedIndividual);
        changeListToFill.add(new AddAxiom(getRootOntology(), propertyAssertionAxiom));
        OWLClassAssertionAxiom classAssertionAxiom = df.getOWLClassAssertionAxiom(type, namedIndividual);
        changeListToFill.add(new AddAxiom(getRootOntology(), classAssertionAxiom));
    }

}
