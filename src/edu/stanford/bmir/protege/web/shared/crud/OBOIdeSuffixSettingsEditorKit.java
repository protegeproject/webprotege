package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.client.irigen.obo.OBOIdSuffixSettingsEditor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class OBOIdeSuffixSettingsEditorKit implements EntityCrudKitEditorKit {

    @Override
    public EntityCrudKitDescriptor getDescriptor() {
        return OBOIdSuffixDescriptor.get();
    }

    @Override
    public EntityCrudKitSuffixSettingsEditor getSuffixSettingsEditor() {
        return new OBOIdSuffixSettingsEditor();
    }
}
