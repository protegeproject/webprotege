package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
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
    
    private String superClsName;
    
    public CreateClassChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String clsName, String superClsName) {
        super(project, userId, changeDescription);
        this.clsName = clsName;
        this.superClsName = superClsName;
    }

    public String getClsName() {
        return clsName;
    }

    public String getSuperClsName() {
        return superClsName;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        OWLDataFactory df = getDataFactory();
        OWLAPIEntityEditorKit kit = getProject().getOWLEntityEditorKit();
        OWLEntityCreatorFactory fac = kit.getEntityCreatorFactory();
        OWLEntityCreator<OWLClass> creator = fac.getEntityCreator(getProject(), getUserId(), clsName, EntityType.CLASS);
        changeListToFill.addAll(creator.getChanges());
        if (superClsName != null) {
            OWLClass subCls = creator.getEntity();
            OWLClass superCls = getRenderingManager().getEntity(superClsName, EntityType.CLASS);
            OWLSubClassOfAxiom ax = df.getOWLSubClassOfAxiom(subCls, superCls);
            AddAxiom addAxiomChange = new AddAxiom(getRootOntology(), ax);
            changeListToFill.add(addAxiomChange);
        }
    }

}
