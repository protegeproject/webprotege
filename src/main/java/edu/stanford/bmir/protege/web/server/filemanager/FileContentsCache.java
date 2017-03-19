package edu.stanford.bmir.protege.web.server.filemanager;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 *
 * A utility class for reading the latest contents of a file into a string.  When the method for getting
 * the contents is called, the fileProvider is checked to ensure that the latest contents are returned.  Contents
 * are cached in memory.
 */
public class FileContentsCache {

    private Provider<File> fileProvider;

    private long lastTimestamp = 0;

    private String cachedTemplate = "";

    public FileContentsCache(@Nonnull Provider<File> fileProvider) {
        this.fileProvider = checkNotNull(fileProvider);
    }

    @Nonnull
    public File getFile() {
        return fileProvider.get();
    }

    public boolean exists() {
        return getFile().exists();
    }

    /**
     * Gets the contents of the specified fileProvider as a string.
     * @return The contents as a string.
     * @throws IOException An exception if there was a problem reading the fileProvider.
     */
    @Nonnull
    public synchronized String getContents() throws IOException {
        File file = getFile();
        if (file.lastModified() > lastTimestamp) {
            cachedTemplate = Files.toString(file, Charsets.UTF_8);
            lastTimestamp = file.lastModified();
        }
        return cachedTemplate;
    }
}
