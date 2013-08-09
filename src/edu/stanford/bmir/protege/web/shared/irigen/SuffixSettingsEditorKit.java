package edu.stanford.bmir.protege.web.shared.irigen;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 * <p>
 *     Binds together
 * </p>
 */
public interface SuffixSettingsEditorKit<S extends SuffixSettings> {

    SuffixSettingsId getSchemeId();

    void createDefaultSettings(AsyncCallback<S> callback);

    SuffixSettingsEditor<S> getEditor();

}
