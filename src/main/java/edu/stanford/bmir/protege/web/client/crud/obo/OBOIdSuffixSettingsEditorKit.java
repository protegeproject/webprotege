package edu.stanford.bmir.protege.web.client.crud.obo;

import edu.stanford.bmir.protege.web.client.crud.obo.OBOIdSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitEditorKit;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixKit;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class OBOIdSuffixSettingsEditorKit implements EntityCrudKitEditorKit {

    @Override
    public EntityCrudKit getDescriptor() {
        return OBOIdSuffixKit.get();
    }

    @Override
    public EntityCrudKitSuffixSettingsEditor getSuffixSettingsEditor() {
        return new OBOIdSuffixSettingsEditor();
    }
}
