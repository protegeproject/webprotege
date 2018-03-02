package edu.stanford.bmir.protege.web.client.crud.obo;

import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitEditorKit;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSuffixSettingsEditor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class OBOIdSuffixSettingsEditorKit implements EntityCrudKitEditorKit {

    @Nonnull
    private final Provider<OBOIdSuffixSettingsEditor> editorProvider;


    @Inject
    public OBOIdSuffixSettingsEditorKit(@Nonnull Provider<OBOIdSuffixSettingsEditor> editorProvider) {
        this.editorProvider = checkNotNull(editorProvider);
    }

    @Override
    public EntityCrudKitSuffixSettingsEditor getSuffixSettingsEditor() {
        return editorProvider.get();
    }
}
