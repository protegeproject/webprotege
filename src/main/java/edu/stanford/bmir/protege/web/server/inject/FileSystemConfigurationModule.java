package edu.stanford.bmir.protege.web.server.inject;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import dagger.Module;
import dagger.Provides;
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
    @TemplatesDirectory
    public File provideTemplatesDirectory(TemplatesDirectoryProvider provider) {
        return provider.get();
    }

    @Provides
    @CommentNotificationTemplate
    public File provideCommentsNotificationTemplateFile(CommentNotificationTemplateProvider provider) {
        return provider.get();
    }

    @Provides
    @CommentNotificationTemplate
    public FileContentsCache providesCommentNotificationTemplate(@CommentNotificationTemplate File templateFile) {
        return new FileContentsCache(templateFile);
    }

    @Provides
    public MustacheFactory providesMustacheFactory() {
        return new DefaultMustacheFactory();
    }
}

