package edu.stanford.bmir.protege.web.server.filemanager;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 *
 * A utility class for reading the latest contents of a file into a string.  When the method for getting
 * the contents is called, the fileSupplier is checked to ensure that the latest contents are returned.  Contents
 * are cached in memory.
 */
public class FileContents implements HasGetFile {

    private Supplier<File> fileSupplier;

    private long lastTimestamp = 0;

    private String cachedTemplate = "";

    public FileContents(@Nonnull Supplier<File> fileSupplier) {
        this.fileSupplier = checkNotNull(fileSupplier);
    }

    @Nonnull
    public File getFile() {
        return fileSupplier.get();
    }

    public boolean exists() {
        return getFile().exists();
    }

    /**
     * Gets the contents of the specified file as a string.
     * @return The contents as a string.
     * @throws IOException An exception if there was a problem reading the file.
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
