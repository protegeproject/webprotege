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
public class CreateInstanceChangeFactory extends OWLOntologyChangeFactory {

    private String individualName;
    
    private String typeName;

    public CreateInstanceChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String individualName, String typeName) {
        super(project, userId, changeDescription);
        this.individualName = individualName;
        this.typeName = typeName;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        OWLAPIEntityEditorKit kit = getProject().getOWLEntityEditorKit();
        OWLEntityCreatorFactory fac = kit.getEntityCreatorFactory();
        OWLEntityCreator<OWLNamedIndividual> ind = fac.getEntityCreator(getProject(), getUserId(), individualName, EntityType.NAMED_INDIVIDUAL);
        changeListToFill.addAll(ind.getChanges());

        OWLClass typeCls = null;
        if(typeName == null) {
            typeCls = getDataFactory().getOWLThing();
        }
        else {
            typeCls = getRenderingManager().getEntity(typeName, EntityType.CLASS);
        }
        OWLClassAssertionAxiom ax = getDataFactory().getOWLClassAssertionAxiom(typeCls, ind.getEntity());
        changeListToFill.add(new AddAxiom(getRootOntology(), ax));
    }

}
