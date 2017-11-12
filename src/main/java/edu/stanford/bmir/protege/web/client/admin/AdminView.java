package edu.stanford.bmir.protege.web.client.admin;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public interface AdminView extends IsWidget {

    void setApplySettingsHandler(@Nonnull Runnable runnable);

    void setRebuildPermissionsHandler(@Nonnull Runnable runnable);

    @Nonnull
    String getApplicationName();

    void setApplicationName(@Nonnull String applicationName);

    @Nonnull
    String getSystemNotificationEmailAddress();

    void setSystemNotificationEmailAddress(@Nonnull String emailAddress);

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

    boolean isNotificationEmailsEnabled();

    void setNotificationEmailsEnabled(boolean enabled);

    void setScheme(@Nonnull SchemeValue scheme);

    @Nonnull
    SchemeValue getScheme();

    void setHost(@Nonnull String host);

    @Nonnull
    String getHost();

    void setPath(@Nonnull String path);

    @Nonnull
    String getPath();

    void setPort(String port);

    String getPort();
}
