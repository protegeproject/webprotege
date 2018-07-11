package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jul 2018
 */
public interface SystemDetailsView extends IsWidget {

    void setApplicationName(@Nonnull String applicationName);

    @Nonnull
    String getApplicationName();

    void setSystemNotificationEmailAddress(@Nonnull String emailAddress);

    @Nonnull
    String getSystemNotificationEmailAddress();

}
