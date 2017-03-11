package edu.stanford.bmir.protege.web.server.inject;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
public class TemplatesDirectoryProvider implements Provider<File> {

    public static final String TEMPLATES_DIRECTORY_NAME = "templates";

    private final File dataDirectory;

    @Inject
    public TemplatesDirectoryProvider(@DataDirectory File dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    public File get() {
        return new File(dataDirectory, TEMPLATES_DIRECTORY_NAME);
    }
}
