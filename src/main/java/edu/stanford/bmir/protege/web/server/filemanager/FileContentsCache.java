package edu.stanford.bmir.protege.web.server.filemanager;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 *
 * A utility class for reading the latest contents of a file into a string.  When the method for getting
 * the contents is called, the file is checked to ensure that the latest contents are returned.  Contents
 * are cached in memory.
 */
public class FileContentsCache {

    private File file;

    private long lastTimestamp = 0;

    private String cachedTemplate = "";

    public FileContentsCache(@Nonnull File file) {
        this.file = checkNotNull(file);
    }

    @Nonnull
    public File getFile() {
        return file;
    }

    public boolean exists() {
        return file.exists();
    }

    /**
     * Gets the contents of the specified file as a string.
     * @return The contents as a string.
     * @throws IOException An exception if there was a problem reading the file.
     */
    @Nonnull
    public synchronized String getContents() throws IOException {
        if (file.lastModified() > lastTimestamp) {
            cachedTemplate = Files.toString(file, Charsets.UTF_8);
            lastTimestamp = file.lastModified();
        }
        return cachedTemplate;
    }
}
