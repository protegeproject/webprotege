package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
public class WebhookSetting implements IsSerializable {

    private String payloadUrl;

    private Set<ProjectWebhookEventType> eventTypes;

    @GwtSerializationConstructor
    private WebhookSetting() {
    }

    public WebhookSetting(String payloadUrl,
                          Set<ProjectWebhookEventType> eventTypes) {
        this.payloadUrl = payloadUrl;
        this.eventTypes = new HashSet<>(eventTypes);
    }

    public String getPayloadUrl() {
        return payloadUrl;
    }

    public Set<ProjectWebhookEventType> getEventTypes() {
        return new HashSet<>(eventTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(payloadUrl, eventTypes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WebhookSetting)) {
            return false;
        }
        WebhookSetting other = (WebhookSetting) obj;
        return this.payloadUrl.equals(other.payloadUrl)
                && this.eventTypes.equals(other.eventTypes);
    }


    @Override
    public String toString() {
        return toStringHelper("WebhookSetting")
                .add("payloadUrl", payloadUrl)
                .add("eventTypes", eventTypes)
                .toString();
    }
}
