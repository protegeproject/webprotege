package edu.stanford.bmir.protege.web.server.inject.project;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class WatchFileProvider implements Provider<File> {

    private File projectDirectory;

    @Inject
    public WatchFileProvider(@ProjectDirectory File projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    @Override
    public File get() {
        return new File(projectDirectory, "watches.csv");
    }
}
