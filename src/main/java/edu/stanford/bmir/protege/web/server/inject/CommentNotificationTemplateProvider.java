package edu.stanford.bmir.protege.web.server.inject;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
public class CommentNotificationTemplateProvider implements Provider<File> {

    public static final String FILE_NAME = "comment-notifications.html";

    @Nonnull
    @TemplatesDirectory
    private File templatesDirectory;

    @Inject
    public CommentNotificationTemplateProvider(@Nonnull @TemplatesDirectory File templatesDirectory) {
        this.templatesDirectory = templatesDirectory;
    }

    @Override
    public File get() {
        templatesDirectory.mkdirs();
        return new File(templatesDirectory, FILE_NAME);
    }
}
