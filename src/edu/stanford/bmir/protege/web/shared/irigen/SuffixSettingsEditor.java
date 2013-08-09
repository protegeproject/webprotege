package edu.stanford.bmir.protege.web.shared.irigen;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 * <p>
 *     An interface to an object, which is a widget, that is responsible for editing a specific kind of {@link SuffixSettings}.
 * </p>
 */
public interface SuffixSettingsEditor<S extends SuffixSettings> extends IsWidget {

    /**
     * Gets the {@link SuffixSettingsId} that identified the type of suffix settings that can be edited by this editor.
     * @return The SuffixSettingsId.  Not {@code null}.
     */
    SuffixSettingsId getSupportedSuffixSettingsId();

    /**
     * Sets the settings that should be edited by this editor.
     * @param settings The settings.  Not {@code null}.
     */
    void setSettings(S settings);

    /**
     * Gets the settings that have been edited by this editor.
     * @return The settings.  Not {@code null}.
     */
    S getSettings();

}
