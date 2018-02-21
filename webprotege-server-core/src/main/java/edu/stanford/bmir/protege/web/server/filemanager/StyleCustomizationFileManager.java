package edu.stanford.bmir.protege.web.server.filemanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 20 Nov 2017
 */
public class StyleCustomizationFileManager {

    /**
     * Style customisation file name
     */
    private static final String WEBPROTEGE_CSS = "webprotege.css";

    private final Logger logger = LoggerFactory.getLogger(StyleCustomizationFileManager.class);

    @Nullable
    private FileContents fileContents;

    public StyleCustomizationFileManager() {
    }

    /**
     * Gets a stylesheet override that can be used to provide customisation of some of the styles used
     * in WebProtege.  An override style sheet should be named webprotege.css and should be
     * placed in the WebProtege config directory.  Changes to the stylesheet on disk will be picked up
     * by WebProtege on a page reload.
     * @return A string containing a stylesheet or the empty string.
     */
    @Nonnull
    public String getStyleCustomization() {
        if(fileContents != null) {
            return fileContents.getContents();
        }
        ConfigDirectorySupplier configDirectorySupplier = new ConfigDirectorySupplier();
        return configDirectorySupplier.getConfigDirectory().map(configDirectory -> {
            Path styleCustomizationFilePath = configDirectory.resolve(WEBPROTEGE_CSS);
            logger.info("Found style customization file at {}", styleCustomizationFilePath.toAbsolutePath());
            fileContents = new FileContents(styleCustomizationFilePath::toFile);
            return fileContents.getContents();
        }).orElse("");
    }
}
