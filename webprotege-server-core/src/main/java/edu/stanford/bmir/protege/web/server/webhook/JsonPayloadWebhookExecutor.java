package edu.stanford.bmir.protege.web.server.webhook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.shared.webhook.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Feb 2018
 */
public class JsonPayloadWebhookExecutor {

    private static final Logger logger = LoggerFactory.getLogger(JsonPayloadWebhookExecutor.class);

    @Nonnull
    private final WebhookExecutor webhookExecutor;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public JsonPayloadWebhookExecutor(@Nonnull WebhookExecutor webhookExecutor,
                                      @Nonnull ObjectMapper objectMapper) {
        this.webhookExecutor = webhookExecutor;
        this.objectMapper = objectMapper;
    }

    /**
     * Submit the specified payload for the specified Webhooks.
     * @param payload The payload that will be serialized to JSON.
     * @param webhooks The Webhooks that specify the payload URLs where the JSON payload
     *                 will be sent to.
     */
    public void submit(@Nonnull Object payload,
                       @Nonnull List<? extends Webhook> webhooks) {
        try {
            final String jsonPayload = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(checkNotNull(payload));
            webhooks.forEach(webhook -> {
                String payloadUrl = webhook.getPayloadUrl();
                String invocationId = UUID.randomUUID().toString();
                webhookExecutor.submit(new WebhookInvocation(invocationId,
                                                             payloadUrl,
                                                             jsonPayload,
                                                             APPLICATION_JSON));
            });
        } catch (JsonProcessingException e) {
            logger.error("Webhook payload serialization error. Payload: {}.  Error: {}",
                         payload,
                         e.getMessage(),
                         e);
        }
    }
}
