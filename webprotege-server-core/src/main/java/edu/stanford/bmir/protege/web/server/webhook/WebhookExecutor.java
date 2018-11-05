package edu.stanford.bmir.protege.web.server.webhook;

import edu.stanford.bmir.protege.web.shared.HasDispose;
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
public class WebhookExecutor implements HasDispose {

    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setName(thread.getName().replace("thread", "webhook-processor-thread"));
        return thread;
    });

    @Inject
    public WebhookExecutor() {
    }

    @Override
    public void dispose() {
        shutdown();
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
