package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.*;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2012
 */
public class CreateDataPropertyChangeFactory extends OWLOntologyChangeFactory {

    private String propertyName;
    
    private String superPropertyName;

    public CreateDataPropertyChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String propertyName, String superPropertyName) {
        super(project, userId, changeDescription);
        this.propertyName = propertyName;
        this.superPropertyName = superPropertyName;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        OWLDataProperty prop = DataFactory.getFreshOWLEntity(EntityType.DATA_PROPERTY, propertyName);
        OWLDataProperty superProperty = null;
        if(superPropertyName == null) {
            superProperty = getDataFactory().getOWLTopDataProperty();
        }
        else {
            superProperty = getRenderingManager().getEntity(superPropertyName, EntityType.DATA_PROPERTY);
        }
        OWLSubDataPropertyOfAxiom ax = getDataFactory().getOWLSubDataPropertyOfAxiom(prop, superProperty);
        changeListToFill.add(new AddAxiom(getRootOntology(), ax));
    }

}
