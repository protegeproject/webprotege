package edu.stanford.bmir.protege.web.server.webhook;

import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.server.filemanager.FileContents;
import edu.stanford.bmir.protege.web.server.templates.TemplateEngine;
import edu.stanford.bmir.protege.web.server.templates.TemplateObjectsBuilder;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 May 2017
 */
@ApplicationSingleton
public class CommentPostedSlackWebhookInvoker implements WebhookInvoker {

    private static final Logger logger = LoggerFactory.getLogger(CommentPostedSlackWebhookInvoker.class);

    private static final String COMMENT_BODY = "comment.body";

    private static final String COMMENT_TS = "comment.ts";

    @Nonnull
    private final ApplicationNameSupplier applicationNameSupplier;

    @Nonnull
    private final WebhookExecutor webhookExecutor;

    @Nonnull
    private final SlackWebhookRepository webhookRepository;

    @Nonnull
    private final PlaceUrl placeUrl;

    @Nonnull
    private final FileContents payloadTemplateFile;

    @Nonnull
    private final TemplateEngine templateEngine;

    @Inject
    public CommentPostedSlackWebhookInvoker(@Nonnull ApplicationNameSupplier applicationNameSupplier,
                                            @Nonnull PlaceUrl placeUrl,
                                            @Nonnull WebhookExecutor webhookExecutor,
                                            @Nonnull SlackWebhookRepository webhookRepository,
                                            @Nonnull @CommentNotificationSlackTemplate FileContents payloadTemplateFile,
                                            @Nonnull TemplateEngine templateEngine) {
        this.applicationNameSupplier = applicationNameSupplier;
        this.placeUrl = placeUrl;
        this.webhookExecutor = webhookExecutor;
        this.webhookRepository = webhookRepository;
        this.payloadTemplateFile = payloadTemplateFile;
        this.templateEngine = templateEngine;
    }

    public void invoke(@Nonnull ProjectId projectId,
                       @Nonnull String projectName,
                       @Nonnull OWLEntityData entityData,
                       @Nonnull Comment comment) {
        String formattedBody = formatBodyUsingSlackFormatting(comment);
        String payloadTemplate = payloadTemplateFile.getContents();
        String entityUrl = placeUrl.getEntityUrl(projectId, entityData.getEntity());
        Map<String, Object> templateObjects = TemplateObjectsBuilder.builder()
                                                                    .withApplicationName(applicationNameSupplier.get())
                                                                    .withUserId(comment.getCreatedBy())
                                                                    .withProjectDisplayName(projectName)
                                                                    .withEntity(entityData)
                                                                    .withEntityUrl(entityUrl)
                                                                    .with(COMMENT_BODY, formattedBody)
                                                                    .with(COMMENT_TS, comment.getCreatedAt() / 1000)
                                                                    .build();
        String jsonPayload = templateEngine.populateTemplate(payloadTemplate, templateObjects);
        webhookRepository.getWebhooks(projectId)
                         .forEach(webhook -> {
                             String payloadUrl = webhook.getPayloadUrl();
                             String invocationId = UUID.randomUUID().toString();
                             webhookExecutor.submit(new WebhookInvocation(invocationId,
                                                                          payloadUrl,
                                                                          jsonPayload,
                                                                          APPLICATION_JSON));
                         });
    }

    static String formatBodyUsingSlackFormatting(@Nonnull Comment comment) {
//        Replace the ampersand, &, with &amp;
//        Replace the less-than sign, < with &lt;
//        Replace the greater-than sign, > with &gt;
        return comment.getBody()
                      .replace("&", "&amp;")
                      .replace("<", "&lt;")
                      .replace(">", "&gt;")
                      .replaceAll("\\[([^\\]]+)\\]\\((.+)\\)", "<$2|$1>");
    }
}
