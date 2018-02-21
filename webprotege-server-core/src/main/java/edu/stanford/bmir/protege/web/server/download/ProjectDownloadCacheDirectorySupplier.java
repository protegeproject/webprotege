package edu.stanford.bmir.protege.web.server.download;

import edu.stanford.bmir.protege.web.server.inject.DataDirectoryProvider;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2017
 */
public class ProjectDownloadCacheDirectorySupplier implements Supplier<Path> {

    private static final String DIRECTORY_NAME = "download-cache";

    private final DataDirectoryProvider dataDirectoryProvider;

    @Inject
    public ProjectDownloadCacheDirectorySupplier(DataDirectoryProvider dataDirectoryProvider) {
        this.dataDirectoryProvider = dataDirectoryProvider;
    }

    @Override
    public Path get() {
        return dataDirectoryProvider.get().toPath().resolve(DIRECTORY_NAME);
    }
}
