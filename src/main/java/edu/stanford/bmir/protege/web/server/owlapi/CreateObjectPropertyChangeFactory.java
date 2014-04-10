package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.user.UserId;
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
        final OWLObjectProperty superProperty;
        if(superPropertyName == null) {
            superProperty = getDataFactory().getOWLTopObjectProperty();
        }
        else {
            superProperty = getRenderingManager().getEntity(superPropertyName, EntityType.OBJECT_PROPERTY);
        }
        OWLObjectProperty prop = DataFactory.getFreshOWLEntity(EntityType.OBJECT_PROPERTY, name);
        OWLSubObjectPropertyOfAxiom ax = getDataFactory().getOWLSubObjectPropertyOfAxiom(prop, superProperty);
        changeListToFill.add(new AddAxiom(getRootOntology(), ax));
        
    }

}
