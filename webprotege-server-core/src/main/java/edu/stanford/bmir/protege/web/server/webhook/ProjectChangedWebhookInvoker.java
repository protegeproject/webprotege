package edu.stanford.bmir.protege.web.server.webhook;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static edu.stanford.bmir.protege.web.shared.webhook.ProjectWebhookEventType.PROJECT_CHANGED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 May 2017
 */
@ProjectSingleton
public class ProjectChangedWebhookInvoker {

    private static final Logger logger = LoggerFactory.getLogger(ProjectChangedWebhookInvoker.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final JsonPayloadWebhookExecutor webhookExecutor;

    @Nonnull
    private final WebhookRepository webhookRepository;


    @Inject
    public ProjectChangedWebhookInvoker(@Nonnull ProjectId projectId,
                                        @Nonnull JsonPayloadWebhookExecutor webhookExecutor,
                                        @Nonnull WebhookRepository webhookRepository) {
        this.projectId = projectId;
        this.webhookExecutor = webhookExecutor;
        this.webhookRepository = webhookRepository;
    }

    public void invoke(@Nonnull UserId userId,
                       @Nonnull RevisionNumber revisionNumber,
                       long timestamp) {
        ProjectChangedWebhookPayload payload = new ProjectChangedWebhookPayload(projectId,
                                                                                userId,
                                                                                revisionNumber.getValueAsInt(),
                                                                                timestamp);

        List<ProjectWebhook> webhooks = webhookRepository.getProjectWebhooks(projectId, PROJECT_CHANGED);
        webhookExecutor.submit(payload, webhooks);
    }
}
