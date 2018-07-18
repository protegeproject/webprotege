package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class WebhookSetting implements IsSerializable {

    public static final String PAYLOAD_URL = "payloadUrl";

    public static final String EVENT_TYPES = "eventTypes";

    @JsonProperty(PAYLOAD_URL)
    @Nonnull
    public abstract String getPayloadUrl();

    @JsonProperty(EVENT_TYPES)
    @Nonnull
    public abstract ImmutableSet<ProjectWebhookEventType> getEventTypes();

    @Nonnull
    public static WebhookSetting get(@Nonnull @JsonProperty(PAYLOAD_URL) String payloadUrl,
                                     @Nonnull @JsonProperty(EVENT_TYPES) Set<ProjectWebhookEventType> eventTypes) {
        return new AutoValue_WebhookSetting(payloadUrl, ImmutableSet.copyOf(eventTypes));
    }
}
