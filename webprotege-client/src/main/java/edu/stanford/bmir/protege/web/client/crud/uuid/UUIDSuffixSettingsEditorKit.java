package edu.stanford.bmir.protege.web.client.crud.uuid;


import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitEditorKit;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSuffixSettingsEditor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class UUIDSuffixSettingsEditorKit implements EntityCrudKitEditorKit {

    @Override
    public EntityCrudKitSuffixSettingsEditor getSuffixSettingsEditor() {
        return new UUIDSuffixSettingsEditor();
    }
}
