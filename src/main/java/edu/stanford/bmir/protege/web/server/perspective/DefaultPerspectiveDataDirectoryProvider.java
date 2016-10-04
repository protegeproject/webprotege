package edu.stanford.bmir.protege.web.server.perspective;

import javax.inject.Inject;
import edu.stanford.bmir.protege.web.server.inject.DataDirectoryProvider;

import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/16
 */
public class DefaultPerspectiveDataDirectoryProvider implements Provider<File> {

    private final DataDirectoryProvider dataDirectoryProvider;

    @javax.inject.Inject
    public DefaultPerspectiveDataDirectoryProvider(DataDirectoryProvider dataDirectoryProvider) {
        this.dataDirectoryProvider = dataDirectoryProvider;
    }

    @Override
    public File get() {
        return new File(dataDirectoryProvider.get(), "default-perspective-data");
    }
}
