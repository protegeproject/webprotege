package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.client.irigen.supplied.SuppliedSuffixSettingsEditor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class SuppliedNameSuffixSettingsEditorKit implements EntityCrudKitEditorKit {

    @Override
    public EntityCrudKitDescriptor getDescriptor() {
        return SuppliedNameSuffixDescriptor.get();
    }

    @Override
    public EntityCrudKitSuffixSettingsEditor getSuffixSettingsEditor() {
        return new SuppliedSuffixSettingsEditor();
    }
}
