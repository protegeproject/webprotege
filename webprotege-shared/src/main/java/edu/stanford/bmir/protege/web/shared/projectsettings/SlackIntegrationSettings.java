package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class SlackIntegrationSettings implements IsSerializable {

    public static final String PAYLOAD_URL = "payloadUrl";

    @JsonProperty(PAYLOAD_URL)
    @Nonnull
    public abstract String getPayloadUrl();

    @JsonCreator
    @Nonnull
    public static SlackIntegrationSettings get(@JsonProperty(PAYLOAD_URL) @Nonnull String payloadUrl) {
        return new AutoValue_SlackIntegrationSettings(payloadUrl);
    }
}
