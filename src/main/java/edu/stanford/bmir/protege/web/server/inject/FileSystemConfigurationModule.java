package edu.stanford.bmir.protege.web.server.inject;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.chgpwd.PasswordResetEmailTemplate;
import edu.stanford.bmir.protege.web.server.filemanager.FileContentsCache;
import edu.stanford.bmir.protege.web.server.issues.CommentNotificationTemplate;
import edu.stanford.bmir.protege.web.server.perspective.DefaultPerspectiveDataDirectory;
import edu.stanford.bmir.protege.web.server.perspective.DefaultPerspectiveDataDirectoryProvider;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveDataCopier;
import edu.stanford.bmir.protege.web.server.project.RootOntologyDocumentFileMatcher;
import edu.stanford.bmir.protege.web.server.project.RootOntologyDocumentMatcherImpl;
import edu.stanford.bmir.protege.web.server.util.TempFileFactory;
import edu.stanford.bmir.protege.web.server.util.TempFileFactoryImpl;

import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class FileSystemConfigurationModule {

    @Provides
    @DataDirectory
    public File provideDataDirectory(DataDirectoryProvider provider) {
        return provider.get();
    }


    @Provides
    @UploadsDirectory
    public File provideUploadsDirectory(UploadsDirectoryProvider provider) {
        return provider.get();
    }

    @Provides
    @DefaultPerspectiveDataDirectory
    public File provideDefaultPerspectiveDataDirectory(DefaultPerspectiveDataDirectoryProvider provider) {
        return provider.get();
    }

    @Provides
    public PerspectiveDataCopier provideDefaultPerspectiveDataCopier(PerspectiveDataCopier copier) {
        return copier;
    }

    @Provides
    public RootOntologyDocumentFileMatcher provideRootOntologyDocumentFileMatcher(RootOntologyDocumentMatcherImpl impl) {
        return impl;
    }

    @Provides
    public TempFileFactory provideTempFileFactory(TempFileFactoryImpl impl) {
        return impl;
    }

    @Provides
    @CommentNotificationTemplate
    public FileContentsCache providesCommentNotificationTemplate(@CommentNotificationTemplate OverridableFile provider) {
        return new FileContentsCache(provider::getTemplateFile);
    }

    @Provides
    @CommentNotificationTemplate
    public OverridableFile provideCommentNotificationTemplateFile(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/comment-notification.html");
    }

    @Provides
    public MustacheFactory providesMustacheFactory() {
        return new DefaultMustacheFactory();
    }

    @Provides
    @PasswordResetEmailTemplate
    public FileContentsCache providesPasswordResetEmailTemplate(@PasswordResetEmailTemplate OverridableFile overridableFile) {
        return new FileContentsCache(overridableFile::getTemplateFile);
    }

    @Provides
    @PasswordResetEmailTemplate
    public OverridableFile providePasswordResetEmailTemplate(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/password-reset-template.html");
    }
}

