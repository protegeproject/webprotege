package edu.stanford.bmir.protege.web.server.inject;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class UploadsDirectoryProvider implements Provider<File> {

    public static final String UPLOADS_DIRECTORY_NAME = "uploads";

    private final File dataDirectory;

    @Inject
    public UploadsDirectoryProvider(@DataDirectory File dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    public File get() {
        return new File(dataDirectory, UPLOADS_DIRECTORY_NAME);
    }
}
