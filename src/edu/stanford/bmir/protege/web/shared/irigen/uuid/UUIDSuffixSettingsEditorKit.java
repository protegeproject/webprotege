package edu.stanford.bmir.protege.web.shared.irigen.uuid;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.irigen.uuid.UUIDSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsEditorKit;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class UUIDSuffixSettingsEditorKit implements SuffixSettingsEditorKit<UUIDSuffixSettings> {

    @Override
    public SuffixSettingsId getSchemeId() {
        return UUIDSuffixSettings.Id;
    }

    @Override
    public void createDefaultSettings(AsyncCallback<UUIDSuffixSettings> callback) {
        callback.onSuccess(new UUIDSuffixSettings());
    }

    @Override
    public UUIDSuffixSettingsEditor getEditor() {
        return new UUIDSuffixSettingsEditor();
    }
}
