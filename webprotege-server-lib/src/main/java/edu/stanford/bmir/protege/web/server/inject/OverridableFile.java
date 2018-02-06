package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
public class OverridableFile implements Supplier<File> {

    private static final Logger logger = LoggerFactory.getLogger(OverridableFile.class);

    private final String relativePathName;

    private final File dataDirectory;

    @Nullable
    private File classPathFile;

    @Inject
    public OverridableFile(@Nonnull String relativePathName,
                           @Nonnull @DataDirectory File dataDirectory) {
        this.dataDirectory = checkNotNull(dataDirectory);
        this.relativePathName = checkNotNull(relativePathName);
    }

    @Nonnull
    public synchronized File get() {
        File overridingFile = new File(dataDirectory, relativePathName);
        if (overridingFile.exists()) {
            logger.info("Providing {} file from overriden file ({})" ,
                        relativePathName,
                        overridingFile.getAbsolutePath());
            return overridingFile;
        }
        else {
            // Load from class path
            Optional<File> fileFromClassPath = getFileFromClassPath();
            logger.info("Providing {} file from class path" ,
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
            URL templateUrl = getClass().getResource("/" + relativePathName);
            if (templateUrl == null) {
                logger.info("Unable to locate {} template file on class path: /", relativePathName);
                return Optional.empty();
            }
            classPathFile = new File(templateUrl.toURI());
            return Optional.of(classPathFile);
        } catch (URISyntaxException e) {
            logger.error("An error occurred whilst attempting to obtain a file from the class path", e);
            return Optional.empty();
        }
    }
}
