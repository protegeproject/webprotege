package edu.stanford.bmir.protege.web.shared.crud.supplied;

import edu.stanford.bmir.protege.web.client.crud.supplied.SuppliedSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitDescriptor;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitEditorKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettingsEditor;

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
