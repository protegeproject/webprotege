package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.owlapi.RootOntologyDocumentFileMatcher;
import edu.stanford.bmir.protege.web.server.owlapi.RootOntologyDocumentMatcherImpl;
import edu.stanford.bmir.protege.web.server.perspective.DefaultPerspectiveDataDirectory;
import edu.stanford.bmir.protege.web.server.perspective.DefaultPerspectiveDataDirectoryProvider;
import edu.stanford.bmir.protege.web.server.util.TempFileFactory;
import edu.stanford.bmir.protege.web.server.util.TempFileFactoryImpl;

import javax.inject.Inject;
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
    public RootOntologyDocumentFileMatcher provideRootOntologyDocumentFileMatcher(RootOntologyDocumentMatcherImpl impl) {
        return impl;
    }

    @Provides
    public TempFileFactory provideTempFileFactory(TempFileFactoryImpl impl) {
        return impl;
    }
}
