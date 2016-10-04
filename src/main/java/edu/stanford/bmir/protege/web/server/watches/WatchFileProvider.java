package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.inject.project.ProjectDirectory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/05/15
 */
public class WatchFileProvider implements Provider<File> {

    public static final String WATCH_FILE_NAME = "watches.csv";

    private File projectDirectory;

    @Inject
    public WatchFileProvider(@ProjectDirectory File projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    @Override
    public File get() {
        return new File(projectDirectory, WATCH_FILE_NAME);
    }
}
