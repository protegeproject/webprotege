package edu.stanford.bmir.protege.web.server.inject.project;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Oct 2016
 */
public class ImportsCacheDirectoryProvider implements Provider<File> {

    @Nonnull
    private final ProjectDirectoryProvider projectDirectoryProvider;

    @Inject
    public ImportsCacheDirectoryProvider(@Nonnull ProjectDirectoryProvider projectDirectoryProvider) {
        this.projectDirectoryProvider = projectDirectoryProvider;
    }

    @Override
    public File get() {
        return new File(projectDirectoryProvider.get(), "imports-cache");
    }
}
