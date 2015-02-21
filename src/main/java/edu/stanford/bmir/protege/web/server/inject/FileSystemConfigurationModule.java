package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import edu.stanford.bmir.protege.web.server.project.DefaultUIConfigurationFileManager;
import edu.stanford.bmir.protege.web.server.project.DefaultUIConfigurationFileManagerImpl;

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
        bind(File.class).annotatedWith(DataDirectory.class)
                .toProvider(DataDirectoryProvider.class);

        bind(File.class).annotatedWith(UiConfigurationDirectory.class)
                .toProvider(UiConfigurationDirectoryProvider.class);

        bind(DefaultUIConfigurationFileManager.class).to(DefaultUIConfigurationFileManagerImpl.class);
    }
}
