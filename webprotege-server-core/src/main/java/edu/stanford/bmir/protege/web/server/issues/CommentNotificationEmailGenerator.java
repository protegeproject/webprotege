package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.server.filemanager.FileContents;
import edu.stanford.bmir.protege.web.server.templates.TemplateEngine;
import edu.stanford.bmir.protege.web.server.templates.TemplateObjectsBuilder;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
public class CommentNotificationEmailGenerator {

    public static final String ENTITY_URL = "entity.url";

    public static final String THREAD = "thread";

    public static final String COMMENT = "comment";

    private final TemplateEngine templateEngine;

    private final FileContents templateFile;

    private static final Logger logger = LoggerFactory.getLogger(CommentNotificationEmailGenerator.class);

    private final PlaceUrl placeUrl;

    private final ApplicationNameSupplier applicationNameSupplier;

    @Inject
    public CommentNotificationEmailGenerator(@Nonnull @CommentNotificationEmailTemplate FileContents templateFile,
                                             @Nonnull TemplateEngine templateEngine,
                                             @Nonnull ApplicationNameSupplier applicationNameSupplier,
                                             @Nonnull PlaceUrl placeUrl) {
        this.templateEngine = templateEngine;
        this.templateFile = templateFile;
        this.placeUrl = placeUrl;
        this.applicationNameSupplier = applicationNameSupplier;
    }

    @Nonnull
    public String generateEmailBody(@Nonnull String projectDisplayName,
                                    @Nonnull OWLEntityData entityData,
                                    @Nonnull EntityDiscussionThread thread,
                                    @Nonnull Comment comment) {
        try {
            String entityUrl = placeUrl.getEntityUrl(thread.getProjectId(),
                                                     thread.getEntity());
            String projectUrl = placeUrl.getProjectUrl(thread.getProjectId());

            ImmutableMap<String, Object> objects =
                    TemplateObjectsBuilder.builder()
                                          .withApplicationName(applicationNameSupplier.get())
                                          .withProjectDisplayName(projectDisplayName)
                                          .withEntity(entityData)
                                          .withProjectUrl(projectUrl)
                                          .with(ENTITY_URL, entityUrl)
                                          .with(THREAD, thread)
                                          .with(COMMENT, comment)
                                          .build();
            String template = templateFile.getContents();
            return templateEngine.populateTemplate(template, objects);
        } catch (Exception e) {
            logger.error("An error occurred whilst generating the body for a comment notification email", e);
            return String.format("Invalid template file: %s (Cause: %s)" ,
                                 templateFile.getFile().getName(),
                                 e.getMessage());
        }
    }
}
