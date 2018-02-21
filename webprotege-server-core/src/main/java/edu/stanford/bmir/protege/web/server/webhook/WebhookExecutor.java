package edu.stanford.bmir.protege.web.server.webhook;

import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 May 2017
 */
@ApplicationSingleton
public class WebhookExecutor {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Inject
    public WebhookExecutor() {
    }

    /**
     * Shuts down this {@link WebhookExecutor}.
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * Submit the specified {@link WebhookInvocation} to be executed.  The {@link WebhookInvocation}
     * will be submitted asynchronously.  Ordering is preserved so that if WebhookInvoation X is submitted
     * before WebhookInvocation Y then X will be invoked before Y.
     * @param webhookInvocation The invocation
     */
    public void submit(@Nonnull WebhookInvocation webhookInvocation) {
        executor.submit(new PostWebhookPayloadTask(webhookInvocation));
    }
}
