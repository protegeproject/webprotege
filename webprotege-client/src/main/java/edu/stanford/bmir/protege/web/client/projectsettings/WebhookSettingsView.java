package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.projectsettings.WebhookSetting;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Jul 2018
 */
public interface WebhookSettingsView extends IsWidget {

    void setWebhookUrls(@Nonnull List<WebhookSetting> webhookSettings);

    @Nonnull
    List<WebhookSetting> getWebhookUrls();
}
