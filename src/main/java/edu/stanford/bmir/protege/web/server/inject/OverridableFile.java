package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
public class OverridableFile {

    private final WebProtegeLogger logger;

    private final String relativePathName;

    private final File dataDirectory;

    @Nullable
    private File classPathFile;

    @Inject
    public OverridableFile(@Nonnull String relativePathName,
                           @Nonnull @DataDirectory File dataDirectory,
                           @Nonnull WebProtegeLogger logger) {
        this.dataDirectory = checkNotNull(dataDirectory);
        this.relativePathName = checkNotNull(relativePathName);
        this.logger = logger;
    }

    @Nonnull
    public synchronized File getTemplateFile() {
        File overridingFile = new File(dataDirectory, relativePathName);
        if (overridingFile.exists()) {
            logger.info("Providing %s file from overriden file (%s)" ,
                        relativePathName,
                        overridingFile.getAbsolutePath());
            return overridingFile;
        }
        else {
            // Load from class path
            Optional<File> fileFromClassPath = getFileFromClassPath();
            logger.info("Providing %s file from class path" ,
                        relativePathName);
            return fileFromClassPath.orElse(overridingFile);
        }
    }

    @Nonnull
    private Optional<File> getFileFromClassPath() {
        try {
            if (classPathFile != null) {
                return Optional.of(classPathFile);
            }
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL templateUrl = classLoader.getResource("/" + relativePathName);
            if (templateUrl == null) {
                logger.info("Unable to locate %s template file on class path: /", relativePathName);
                return Optional.empty();
            }
            classPathFile = new File(templateUrl.toURI());
            return Optional.of(classPathFile);
        } catch (URISyntaxException e) {
            logger.severe(e);
            return Optional.empty();
        }
    }
}
