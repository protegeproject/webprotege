package edu.stanford.bmir.protege.web.server.inject;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.chgpwd.PasswordResetEmailTemplate;
import edu.stanford.bmir.protege.web.server.filemanager.FileContents;
import edu.stanford.bmir.protege.web.server.issues.CommentNotificationEmailTemplate;
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
    public MustacheFactory providesMustacheFactory() {
        return new DefaultMustacheFactory();
    }

    @Provides
    @CommentNotificationEmailTemplate
    public OverridableFile provideCommentNotificationTemplateFile(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/comment-notification.html");
    }

    @Provides
    @CommentNotificationEmailTemplate
    public FileContents providesCommentNotificationTemplate(@CommentNotificationEmailTemplate OverridableFile file) {
        return new FileContents(file);
    }

    @Provides
    @PasswordResetEmailTemplate
    public OverridableFile providePasswordResetEmailTemplate(OverridableFileFactory factory) {
        return factory.getOverridableFile("templates/password-reset-template.html");
    }

    @Provides
    @PasswordResetEmailTemplate
    public FileContents providesPasswordResetEmailTemplate(@PasswordResetEmailTemplate OverridableFile file) {
        return new FileContents(file);
    }
}

