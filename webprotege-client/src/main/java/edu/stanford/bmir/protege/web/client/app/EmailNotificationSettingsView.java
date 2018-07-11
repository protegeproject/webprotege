package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jul 2018
 */
public interface EmailNotificationSettingsView extends IsWidget {

    boolean isNotificationEmailsEnabled();

    void setNotificationEmailsEnabled(boolean enabled);
}
