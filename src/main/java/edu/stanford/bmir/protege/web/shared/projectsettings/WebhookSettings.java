package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.util.*;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
public class WebhookSettings implements IsSerializable {

    private Set<WebhookSetting> webhookSettings;

    @GwtSerializationConstructor
    private WebhookSettings() {
    }

    public WebhookSettings(List<WebhookSetting> webhookSettings) {
        this.webhookSettings = new LinkedHashSet<>(webhookSettings);
    }

    public List<WebhookSetting> getWebhookSettings() {
        return new ArrayList<>(webhookSettings);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(webhookSettings);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WebhookSettings)) {
            return false;
        }
        WebhookSettings other = (WebhookSettings) obj;
        return this.webhookSettings.equals(other.webhookSettings);
    }


    @Override
    public String toString() {
        return toStringHelper("WebhookSettings")
                .addValue(webhookSettings)
                .toString();
    }
}
