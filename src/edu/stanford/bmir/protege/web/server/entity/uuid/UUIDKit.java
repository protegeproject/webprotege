package edu.stanford.bmir.protege.web.server.entity.uuid;

import edu.stanford.bmir.protege.web.server.entity.OWLEntityCrudKit;
import edu.stanford.bmir.protege.web.server.entity.OWLEntityEditorKitFactory;
import edu.stanford.bmir.protege.web.server.entity.OWLEntityManagerSettings;
import edu.stanford.bmir.protege.web.server.entity.ShortFormSetter;
import edu.stanford.bmir.protege.web.server.irigen.EntityIRIGenerator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public class UUIDKit extends OWLEntityCrudKit {

    private UUIDEntityManagerSettings settings;

    public UUIDKit() {
    }

    public UUIDKit(UUIDEntityManagerSettings settings) {
        this.settings = settings;
    }

    @Override
    public OWLEntityEditorKitFactory getFactory() {
        return null;
    }

    @Override
    public OWLEntityManagerSettings getSettings() {
        return null;
    }


    @Override
    public String getShortForm(OWLEntity entity, OWLAPIProject project) {
        return null;
    }

    @Override
    public ShortFormSetter setShortForm(OWLEntity entity, OWLAPIProject project, String shortForm) {
        return null;
    }

    @Override
    public EntityIRIGenerator getIRIGenerator() {
        return null;
    }
}
