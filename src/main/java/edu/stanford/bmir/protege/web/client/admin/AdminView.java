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

    @Nonnull
    String getApplicationName();

    void setApplicationName(@Nonnull String applicationName);

    @Nonnull
    String getApplicationLogo();

    void setApplicationLogo(@Nonnull String applicationLogo);

    @Nonnull
    String getAdminEmailAddress();

    void setAdminEmailAddress(@Nonnull String adminEmailAddress);

    boolean isAccountCreationAllowed();

    void setAccountCreationAllowed(boolean allowed);

    boolean isProjectCreationAllowed();

    void setProjectCreationAllowed(boolean allowed);

    boolean isProjectUploadAllowed();

    void setProjectUploadAllowed(boolean allowed);

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
