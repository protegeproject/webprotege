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
public class AddSuperClassChangeFactory extends OWLOntologyChangeFactory {

    private String clsName;
    
    private String superClsName;

    public AddSuperClassChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String clsName, String superClsName) {
        super(project, userId, changeDescription);
        this.clsName = clsName;
        this.superClsName = superClsName;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        RenderingManager rm = getRenderingManager();
        OWLClass subCls = rm.getEntity(clsName, EntityType.CLASS);
        OWLClass superCls = rm.getEntity(superClsName, EntityType.CLASS);
        OWLSubClassOfAxiom ax = getDataFactory().getOWLSubClassOfAxiom(subCls, superCls);
        changeListToFill.add(new AddAxiom(getRootOntology(), ax));
    }

}
