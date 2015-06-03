package edu.stanford.bmir.protege.web.server.inject.project;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.nio.file.Paths;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class ChangeHistoryFileProvider implements Provider<File> {

    private static final String CHANGE_DATA_DIRECTORY_NAME = "change-data";

    private static final String CHANGE_DATA_FILE_NAME = "change-data.binary";

    private final File projectDirectory;


    @Inject
    public ChangeHistoryFileProvider(@ProjectDirectory File projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    @Override
    public File get() {
        return new File(new File(projectDirectory, CHANGE_DATA_DIRECTORY_NAME), CHANGE_DATA_FILE_NAME);
    }
}
