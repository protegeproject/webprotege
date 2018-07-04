package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Jul 2018
 */
public interface SlackWebhookSettingsView extends IsWidget {

    void setWebhookUrl(@Nonnull String url);

    @Nonnull
    String getWebhookrUrl();
}
