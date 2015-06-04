package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import edu.stanford.bmir.protege.web.server.owlapi.RootOntologyDocumentFileMatcher;
import edu.stanford.bmir.protege.web.server.owlapi.RootOntologyDocumentMatcherImpl;
import edu.stanford.bmir.protege.web.server.project.DefaultUIConfigurationFileManager;
import edu.stanford.bmir.protege.web.server.project.DefaultUIConfigurationFileManagerImpl;
import edu.stanford.bmir.protege.web.server.util.TempFileFactory;
import edu.stanford.bmir.protege.web.server.util.TempFileFactoryImpl;

import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class FileSystemConfigurationModule extends AbstractModule {

    public FileSystemConfigurationModule() {
    }

    @Override
    protected void configure() {

        bind(File.class).annotatedWith(DataDirectory.class).toProvider(DataDirectoryProvider.class);

        bind(File.class).annotatedWith(UploadsDirectory.class).toProvider(UploadsDirectoryProvider.class);

        bind(File.class).annotatedWith(DefaultUiConfigurationDirectory.class)
                .toProvider(DefaultUiConfigurationDirectoryProvider.class);

        bind(DefaultUIConfigurationFileManager.class).to(DefaultUIConfigurationFileManagerImpl.class);

        bind(RootOntologyDocumentFileMatcher.class)
                .to(RootOntologyDocumentMatcherImpl.class);

        bind(TempFileFactory.class)
                .to(TempFileFactoryImpl.class);

    }
}
