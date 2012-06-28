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
public class CreateAnnotationPropertyChangeFactory extends OWLOntologyChangeFactory {

    private String propertyName;
    
    private String superPropertyName;

    public CreateAnnotationPropertyChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String property, String superProperty) {
        super(project, userId, changeDescription);
        this.propertyName = property;
        this.superPropertyName = superProperty;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        OWLAPIEntityEditorKit kit = getProject().getOWLEntityEditorKit();
        OWLEntityCreatorFactory fac = kit.getEntityCreatorFactory();
        OWLEntityCreator<OWLAnnotationProperty> prop = fac.getEntityCreator(getProject(), getUserId(), propertyName, EntityType.ANNOTATION_PROPERTY);
        changeListToFill.addAll(prop.getChanges());

        if(superPropertyName != null) {
            OWLAnnotationProperty superProperty = getRenderingManager().getEntity(superPropertyName, EntityType.ANNOTATION_PROPERTY);
            OWLSubAnnotationPropertyOfAxiom ax = getDataFactory().getOWLSubAnnotationPropertyOfAxiom(prop.getEntity(), superProperty);
            changeListToFill.add(new AddAxiom(getRootOntology(), ax));
        }

    }

}
