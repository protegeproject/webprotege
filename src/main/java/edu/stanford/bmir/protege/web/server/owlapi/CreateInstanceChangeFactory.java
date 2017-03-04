package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.project.Project;
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
public class CreateInstanceChangeFactory extends OWLOntologyChangeFactory {

    private String individualName;
    
    private String typeName;

    public CreateInstanceChangeFactory(Project project, UserId userId, String changeDescription, String individualName, String typeName) {
        super(project, userId, changeDescription);
        this.individualName = individualName;
        this.typeName = typeName;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        final OWLClass typeCls;
        if(typeName == null) {
            typeCls = getDataFactory().getOWLThing();
        }
        else {
            typeCls = getRenderingManager().getEntity(typeName, EntityType.CLASS);
        }
        OWLNamedIndividual ind = DataFactory.getFreshOWLEntity(EntityType.NAMED_INDIVIDUAL, individualName);
        OWLClassAssertionAxiom ax = getDataFactory().getOWLClassAssertionAxiom(typeCls, ind);
        changeListToFill.add(new AddAxiom(getRootOntology(), ax));
    }

}
