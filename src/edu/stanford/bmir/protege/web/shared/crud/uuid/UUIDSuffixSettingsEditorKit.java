package edu.stanford.bmir.protege.web.shared.crud.uuid;


import edu.stanford.bmir.protege.web.client.crud.uuid.UUIDSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitDescriptor;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitEditorKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettingsEditor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class UUIDSuffixSettingsEditorKit implements EntityCrudKitEditorKit {

    @Override
    public EntityCrudKitDescriptor getDescriptor() {
        return UUIDSuffixDescriptor.get();
    }

    @Override
    public EntityCrudKitSuffixSettingsEditor getSuffixSettingsEditor() {
        return new UUIDSuffixSettingsEditor();
    }
}
