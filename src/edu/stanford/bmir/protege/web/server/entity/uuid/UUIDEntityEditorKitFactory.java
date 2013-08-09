package edu.stanford.bmir.protege.web.server.entity.uuid;

import edu.stanford.bmir.protege.web.server.entity.OWLEntityCrudKit;
import edu.stanford.bmir.protege.web.server.entity.OWLEntityEditorKitFactory;
import edu.stanford.bmir.protege.web.server.entity.OWLEntityManagerSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public class UUIDEntityEditorKitFactory extends OWLEntityEditorKitFactory {

    @Override
    public OWLEntityCrudKit createOWLEntityEditorKit() {
        return new UUIDKit();
    }

    @Override
    public OWLEntityCrudKit createOWLEntityEditorKit(OWLEntityManagerSettings settings) {
        return new UUIDKit((UUIDEntityManagerSettings) settings);
    }

    @Override
    public OWLEntityManagerSettings getDefaultSettings() {
        return null;
    }
}
