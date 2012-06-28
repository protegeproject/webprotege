package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2012
 * 
 * Named after some methods on the OntologyService interface.  Basically, adds annotations to classes.
 */
public class AddClassPropertyChangeFactory extends OWLOntologyChangeFactory {

    private String clsName;
    
    private String propertyName;

    private EntityData propertyValue;

    public AddClassPropertyChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String clsName, String propertyName, EntityData propertyValue) {
        super(project, userId, changeDescription);
        this.clsName = clsName;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        RenderingManager rm = getRenderingManager();

    }
}
