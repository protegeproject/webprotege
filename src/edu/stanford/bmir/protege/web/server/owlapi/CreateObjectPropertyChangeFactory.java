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
public class CreateObjectPropertyChangeFactory extends OWLOntologyChangeFactory {

    private String name;
    
    private String superPropertyName;

    public CreateObjectPropertyChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String name, String superPropertyName) {
        super(project, userId, changeDescription);
        this.name = name;
        this.superPropertyName = superPropertyName;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        OWLAPIEntityEditorKit kit = getProject().getOWLEntityEditorKit();
        OWLEntityCreatorFactory fac = kit.getEntityCreatorFactory();
        OWLEntityCreator<OWLObjectProperty> prop = fac.getEntityCreator(getProject(), getUserId(), name, EntityType.OBJECT_PROPERTY);
        changeListToFill.addAll(prop.getChanges());
        
        OWLObjectProperty superProperty;
        if(superPropertyName == null) {
            superProperty = getDataFactory().getOWLTopObjectProperty();
        }
        else {
            superProperty = getRenderingManager().getEntity(superPropertyName, EntityType.OBJECT_PROPERTY);
        }
        OWLSubObjectPropertyOfAxiom ax = getDataFactory().getOWLSubObjectPropertyOfAxiom(prop.getEntity(), superProperty);
        changeListToFill.add(new AddAxiom(getRootOntology(), ax));
        
    }

}
