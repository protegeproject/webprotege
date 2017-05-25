package edu.stanford.bmir.protege.web.server.webhook;

import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 May 2017
 */
public class PostWebhookPayloadTask implements Callable<Void> {

    private static final Logger logger = LoggerFactory.getLogger(PostWebhookPayloadTask.class);

    @Nonnull
    private final WebhookInvocation invocation;

    public PostWebhookPayloadTask(@Nonnull WebhookInvocation invocation) {
        this.invocation = checkNotNull(invocation);
    }

    @Override
    public Void call() throws Exception {
        logger.info("Posting webhook payload {} to {}",
                    invocation.getId(),
                    invocation.getPayloadUrl());

        Request.Post(invocation.getPayloadUrl())
               .bodyString(invocation.getPayload(), invocation.getContentType())
               .execute()
               .handleResponse(httpResponse -> {
                   logger.info("Webhook payload {} POST result {}",
                               invocation.getId(),
                               httpResponse.getStatusLine().getStatusCode());
                   return null;
               });
        return null;
    }
}
