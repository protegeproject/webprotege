package edu.stanford.bmir.protege.web.client.crud.obo;

import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitEditorKit;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSuffixSettingsEditor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class OBOIdSuffixSettingsEditorKit implements EntityCrudKitEditorKit {

    @Override
    public EntityCrudKitSuffixSettingsEditor getSuffixSettingsEditor() {
        return new OBOIdSuffixSettingsEditor();
    }
}
