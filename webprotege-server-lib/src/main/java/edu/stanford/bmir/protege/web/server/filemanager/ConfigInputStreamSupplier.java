package edu.stanford.bmir.protege.web.server.filemanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 3 Nov 2017.
 *
 * A supplier of input streams for config files.  Config files are simply files that are either located
 * in the WebProtege config directory (see {@link ConfigDirectorySupplier}) or on the WebProtege class
 * path.  Examples of config files include webprotege.properties and mail.properties.  The class path
 * version of a config file represents the default version of that config file and it is expected that
 * this default version is present.
 */
public class ConfigInputStreamSupplier {

    private static final Logger logger = LoggerFactory.getLogger(ConfigInputStreamSupplier.class);

    @Nonnull
    private final ConfigDirectorySupplier configDirectorySupplier;

    @Inject
    public ConfigInputStreamSupplier(@Nonnull ConfigDirectorySupplier configDirectorySupplier) {
        this.configDirectorySupplier = checkNotNull(configDirectorySupplier);
    }

    /**
     * Gets an input stream for the specified config file. The config directory (as supplied by
     * {@link ConfigDirectorySupplier#getConfigDirectory()} will first be searched for the file, and
     * then after this the WebProtege class path will be searched for the file.  This config file MUST
     * therefore exist in the class path (e.g. webprotege.properties or mail.properties) as a default.
     * @param fileName The file name of the config file.
     * @return An input stream for reading the config file.  The caller is responsible for closing this
     * stream.
     * @throws IllegalArgumentException if there is no file with the specified file name on the class path.
     */
    @Nonnull
    public BufferedInputStream getConfigFileInputStream(@Nonnull String fileName) throws IOException {
        Optional<Path> configDirectory = configDirectorySupplier.getConfigDirectory();
        if (configDirectory.isPresent()) {
            Path configDirectoryPath = configDirectory.get();
            Path configFilePath = configDirectoryPath.resolve(fileName);
            if(Files.exists(configFilePath)) {
                logger.info("Loading {} from {}", fileName, configFilePath.toAbsolutePath());
                return new BufferedInputStream(Files.newInputStream(configFilePath));
            }
        }
        logger.info("Loading {} from the class path", fileName);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if(inputStream == null) {
            throw new IllegalArgumentException(
                    String.format("Invalid config file name (%s).  " +
                            "This file was not found on the class path.",
                            fileName)
            );
        }
        else {
            return new BufferedInputStream(inputStream);
        }
    }

}
