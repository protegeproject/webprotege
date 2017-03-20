package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.app.ApplicationNameProvider;
import edu.stanford.bmir.protege.web.server.filemanager.FileContents;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.place.PlaceUrl;
import edu.stanford.bmir.protege.web.server.templates.TemplateEngine;
import edu.stanford.bmir.protege.web.server.templates.TemplateObjectsBuilder;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
public class CommentNotificationEmailGenerator {

    public static final String PROJECT_DISPLAY_NAME = "project.displayName";

    public static final String PROJECT_URL = "project.url";

    public static final String ENTITY_URL = "entity.url";

    public static final String THREAD = "thread";

    public static final String COMMENT = "comment";

    public static final String APPLICATION_NAME = "application.name";


    private final TemplateEngine templateEngine;

    private final FileContents templateFile;

    private final WebProtegeLogger webProtegeLogger;

    private final PlaceUrl placeUrl;

    private final ApplicationNameProvider applicationNameProvider;

    @Inject
    public CommentNotificationEmailGenerator(@Nonnull @CommentNotificationEmailTemplate FileContents templateFile,
                                             @Nonnull TemplateEngine templateEngine,
                                             @Nonnull ApplicationNameProvider applicationNameProvider,
                                             @Nonnull PlaceUrl placeUrl,
                                             @Nonnull WebProtegeLogger webProtegeLogger) {
        this.templateEngine = templateEngine;
        this.templateFile = templateFile;
        this.webProtegeLogger = webProtegeLogger;
        this.placeUrl = placeUrl;
        this.applicationNameProvider = applicationNameProvider;
    }

    @Nonnull
    public String generateEmailBody(@Nonnull String projectDisplayName,
                                    @Nonnull EntityDiscussionThread thread,
                                    @Nonnull Comment comment) {
        try {
            String entityUrl = placeUrl.getEntityUrl(thread.getProjectId(),
                                                     thread.getEntity());
            String projectUrl = placeUrl.getProjectUrl(thread.getProjectId());

            ImmutableMap<String, Object> objects =
                    TemplateObjectsBuilder.builder()
                                          .withApplicationName(applicationNameProvider.getApplicationName())
                                          .withProjectDisplayName(projectDisplayName)
                                          .withProjectUrl(projectUrl)
                                          .with(ENTITY_URL, entityUrl)
                                          .with(THREAD, thread)
                                          .with(COMMENT, comment)
                                          .build();
            String template = templateFile.getContents();
            return templateEngine.populateTemplate(template, objects);
        } catch (Exception e) {
            webProtegeLogger.severe(e);
            return String.format("Invalid template file: %s (Cause: %s)" ,
                                 templateFile.getFile().getName(),
                                 e.getMessage());
        }
    }
}
