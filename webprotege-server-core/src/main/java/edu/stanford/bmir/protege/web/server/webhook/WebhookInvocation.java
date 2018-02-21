package edu.stanford.bmir.protege.web.server.webhook;

import org.apache.http.entity.ContentType;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 May 2017
 */
public class WebhookInvocation {

    private final String id;

    @Nonnull
    private final String payloadUrl;

    @Nonnull
    private final String payload;

    @Nonnull
    private final ContentType contentType;

    public WebhookInvocation(@Nonnull String id,
                             @Nonnull String payloadUrl,
                             @Nonnull String payload,
                             @Nonnull ContentType contentType) {
        this.id = checkNotNull(id);
        this.payloadUrl = checkNotNull(payloadUrl);
        this.payload = checkNotNull(payload);
        this.contentType = checkNotNull(contentType);
    }

    public String getId() {
        return id;
    }

    @Nonnull
    public String getPayloadUrl() {
        return payloadUrl;
    }

    @Nonnull
    public String getPayload() {
        return payload;
    }

    @Nonnull
    public ContentType getContentType() {
        return contentType;
    }
}
