package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2012
 */
public class CreateClassChangeFactory extends OWLOntologyChangeFactory {

    private String clsName;
    
    private OWLClass superCls;

    public CreateClassChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String clsName) {
        this(project, userId, changeDescription, clsName, null);
    }


    public CreateClassChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String clsName, OWLClass superCls) {
        super(project, userId, changeDescription);
        this.clsName = clsName;
        this.superCls = superCls;
    }


    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        OWLDataFactory df = getDataFactory();
        OWLClass cls = DataFactory.getFreshOWLEntity(EntityType.CLASS, clsName);
        if (superCls != null) {
            OWLClass subCls = cls;
            OWLSubClassOfAxiom ax = df.getOWLSubClassOfAxiom(subCls, superCls);
            AddAxiom addAxiomChange = new AddAxiom(getRootOntology(), ax);
            changeListToFill.add(addAxiomChange);
        }
        else {
            changeListToFill.add(new AddAxiom(getRootOntology(), df.getOWLDeclarationAxiom(cls)));
        }
    }

}
