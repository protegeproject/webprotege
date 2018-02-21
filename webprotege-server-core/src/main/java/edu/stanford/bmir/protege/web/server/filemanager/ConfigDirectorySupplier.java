package edu.stanford.bmir.protege.web.server.filemanager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 3 Nov 2017
 */
public class ConfigDirectorySupplier {

    /**
     * The config directory root for Unix systems (Linux and MacOS)
     */
    private static final String UNIX_ROOT = "/etc";

    /**
     * The config directory parent root for Windows systems.  Note
     * that the path is not case sensitive, but Windows typically capitalises names.
     */
    private static final String WINDOWS_ROOT = "C:/ProgramData";

    /**
     * The webprotege config directory.  Case sensitive on Unix and Linux but not on Windows or MacOS.
     */
    private static final String WEBPROTEGE = "webprotege";

    @Inject
    public ConfigDirectorySupplier() {
    }

    /**
     * Provides the WebProtege configuration directory.  On Unix, Linux, OS X and MacOS this
     * is /etc/webprotege.  On Windows (assuming a default drive of C:) this is C:\ProgramData\webprotege.
     * @return The config directory path.  If this config directory does not exist then an {@link Optional#empty()}
     * value will be returned.
     */
    @Nonnull
    public Optional<Path> getConfigDirectory() {
        Path nixConfigPath = Paths.get(UNIX_ROOT, WEBPROTEGE);
        if(Files.exists(nixConfigPath)) {
            return Optional.of(nixConfigPath);
        }
        Path windowsConfigPath = Paths.get(WINDOWS_ROOT, WEBPROTEGE);
        if(Files.exists(windowsConfigPath)) {
            return Optional.of(windowsConfigPath);
        }
        return Optional.empty();
    }
}
