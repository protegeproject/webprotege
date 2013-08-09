package edu.stanford.bmir.protege.web.shared.irigen.supplied;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.irigen.supplied.SuppliedSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsId;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsEditorKit;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class SuppliedSuffixSettingsEditorKit implements SuffixSettingsEditorKit<SuppliedSuffixSettings> {

    @Override
    public SuffixSettingsId getSchemeId() {
        return SuppliedSuffixSettings.Id;
    }

    @Override
    public void createDefaultSettings(AsyncCallback<SuppliedSuffixSettings> callback) {
        callback.onSuccess(new SuppliedSuffixSettings());
    }

    @Override
    public SuppliedSuffixSettingsEditor getEditor() {
        return new SuppliedSuffixSettingsEditor();
    }
}
