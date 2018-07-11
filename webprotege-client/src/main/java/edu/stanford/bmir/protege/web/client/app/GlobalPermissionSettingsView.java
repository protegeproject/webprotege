package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jul 2018
 */
public interface GlobalPermissionSettingsView extends IsWidget {

    boolean isAccountCreationAllowed();

    void setAccountCreationAllowed(boolean allowed);

    boolean isProjectCreationAllowed();

    void setProjectCreationAllowed(boolean allowed);

    boolean isProjectUploadAllowed();

    void setProjectUploadAllowed(boolean allowed);

    /**
     * Gets the max upload size.
     * @return The max upload size in MB represented as a string.
     */
    String getMaxUploadSize();

    /**
     * Sets the max upload size.
     * @param maxUploadSize The max upload size in MB represented as a String
     */
    void setMaxUploadSize(String maxUploadSize);
}
