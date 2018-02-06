package edu.stanford.bmir.protege.web.shared.webhook;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.mongodb.morphia.annotations.*;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.webhook.SlackWebhook.PAYLOAD_URL;
import static edu.stanford.bmir.protege.web.shared.webhook.SlackWebhook.PROJECT_ID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
@Entity(noClassnameStored = true, value = "SlackWebhooks")
@Indexes(
        value = {
                @Index(
                        fields = {@Field(PROJECT_ID), @Field(PAYLOAD_URL)},
                        options = @IndexOptions(unique = true)
                )
        }
)
public class SlackWebhook implements Webhook, IsSerializable {


    public static final String PROJECT_ID = "projectId";

    public static final String PAYLOAD_URL = "payloadUrl";

    private ProjectId projectId;

    private String payloadUrl;

    @GwtSerializationConstructor
    private SlackWebhook() {
    }

    public SlackWebhook(@Nonnull ProjectId projectId,
                        @Nonnull String payloadUrl) {
        this.projectId = checkNotNull(projectId);
        this.payloadUrl = checkNotNull(payloadUrl);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public String getPayloadUrl() {
        return payloadUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, payloadUrl);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SlackWebhook)) {
            return false;
        }
        SlackWebhook other = (SlackWebhook) obj;
        return this.projectId.equals(other.projectId)
                && this.payloadUrl.equals(other.payloadUrl);
    }


    @Override
    public String toString() {
        return toStringHelper("SlackWebhook")
                .addValue(projectId)
                .add("payloadUrl", payloadUrl)
                .toString();
    }
}
