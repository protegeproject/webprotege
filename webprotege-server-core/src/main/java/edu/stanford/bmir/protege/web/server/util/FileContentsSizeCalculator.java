package edu.stanford.bmir.protege.web.server.util;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Apr 2018
 */
public class FileContentsSizeCalculator {

    @Nonnull
    private final ZipInputStreamChecker checker;

    @Inject
    public FileContentsSizeCalculator(@Nonnull ZipInputStreamChecker checker) {
        this.checker = checkNotNull(checker);
    }

    /**
     * Gets the size of the uploaded file.  If the file is a zip file then the size of the
     * expanded contents is computed.
     * @param file The file.
     * @return The size of the contents
     * @throws IOException If there was a problem reading the file or examining its contents.
     */
    public long getContentsSize(@Nonnull File file) throws IOException {
        if (!checker.isZipFile(file)) {
            return file.length();
        }
        try(ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            long totalSize = 0;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                long size = entry.getSize();
                totalSize += size;
            }
            return totalSize;
        }
    }
}
