package edu.stanford.bmir.protege.web.server.inject.project;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/06/15
 */
public class ProjectSpecificUiConfigurationDataDirectoryProvider implements Provider<File> {

    private static final String CONFIGURATION_DATA_DIRECTORY_NAME = "configuration-data";

    private final File projectDirectory;

    @Inject
    public ProjectSpecificUiConfigurationDataDirectoryProvider(@ProjectDirectory File projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    @Override
    public File get() {
        return new File(projectDirectory, CONFIGURATION_DATA_DIRECTORY_NAME);
    }
}
