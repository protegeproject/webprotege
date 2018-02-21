package edu.stanford.bmir.protege.web.shared.projectsettings;

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
public class SlackIntegrationSettings implements IsSerializable {

    private String payloadUrl;

    @GwtSerializationConstructor
    private SlackIntegrationSettings() {
    }

    public SlackIntegrationSettings(@Nonnull String payloadUrl) {
        this.payloadUrl = checkNotNull(payloadUrl);
    }

    @Nonnull
    public String getPayloadUrl() {
        return payloadUrl;
    }

    @Override
    public int hashCode() {
        return payloadUrl.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SlackIntegrationSettings)) {
            return false;
        }
        SlackIntegrationSettings other = (SlackIntegrationSettings) obj;
        return this.payloadUrl.equals(other.payloadUrl);
    }


    @Override
    public String toString() {
        return toStringHelper("SlackIntegrationSettings")
                .add("payloadUrl", payloadUrl)
                .toString();
    }
}
