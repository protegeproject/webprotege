package edu.stanford.bmir.protege.web.server.webhook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.UUID;

import static edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType.PROJECT_CHANGED;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 May 2017
 */
@ProjectSingleton
public class ProjectChangedWebhookInvoker {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final WebhookExecutor webhookExecutor;

    @Nonnull
    private final WebhookRepository webhookRepository;

    @Inject
    public ProjectChangedWebhookInvoker(@Nonnull ProjectId projectId,
                                        @Nonnull WebhookExecutor webhookExecutor,
                                        @Nonnull WebhookRepository webhookRepository) {
        this.projectId = projectId;
        this.webhookExecutor = webhookExecutor;
        this.webhookRepository = webhookRepository;
    }

    public void invoke(@Nonnull UserId userId,
                       @Nonnull RevisionNumber revisionNumber,
                       long timestamp) {
        ProjectChangedWebhookPayload payload = new ProjectChangedWebhookPayload(projectId.getId(),
                                                                                userId.getUserName(),
                                                                                revisionNumber.getValueAsInt(),
                                                                                timestamp);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String jsonPayload = gson.toJson(payload);
        webhookRepository.getProjectWebhooks(projectId, PROJECT_CHANGED)
                .forEach( webhook -> {
                    String payloadUrl = webhook.getPayloadUrl();
                    String invocationId = UUID.randomUUID().toString();
                    webhookExecutor.submit(new WebhookInvocation(invocationId,
                                                                 payloadUrl,
                                                                 jsonPayload,
                                                                 APPLICATION_JSON));
                });
    }
}
