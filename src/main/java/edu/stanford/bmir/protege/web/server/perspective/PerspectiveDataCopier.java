package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.auth.Md5MessageDigestAlgorithm;
import org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Apr 16
 */
public class PerspectiveDataCopier {

    private static final String DEFAULT_PERSPECTIVE_DATA_RESOURCE_PATH = "/default-perspective-data";

    private final WebProtegeLogger logger;

    private final File defaultPerspectiveDataDirectory;



    @Inject
    public PerspectiveDataCopier(@DefaultPerspectiveDataDirectory File defaultPerspectiveDataDirectory,
                                 WebProtegeLogger logger) {
        this.defaultPerspectiveDataDirectory = defaultPerspectiveDataDirectory;
        this.logger = logger;
    }

    public void copyDefaultPerspectiveData() {
        try {
            URL url = getClass().getResource(DEFAULT_PERSPECTIVE_DATA_RESOURCE_PATH);
            File templateDefaultPerspectiveDataDirectory = new File(url.toURI());
            defaultPerspectiveDataDirectory.getParentFile().mkdirs();
            FileUtils.copyDirectory(templateDefaultPerspectiveDataDirectory, defaultPerspectiveDataDirectory, false);
            logger.info("Copied default perspective data to %s (from %s)",
                        defaultPerspectiveDataDirectory,
                        templateDefaultPerspectiveDataDirectory);
            File[] perspectiveFiles = defaultPerspectiveDataDirectory.listFiles();
            if (perspectiveFiles != null) {
                for(File file : perspectiveFiles) {
                    String fileName = file.getName();
                    if(fileName.endsWith(".json") && !fileName.equals("perspective.list.json")) {
                        String strippedFileName = fileName.substring(0, fileName.length() - 5);
                        file.renameTo(new File(defaultPerspectiveDataDirectory, computeMD5(strippedFileName) + ".json"));
                    }
                }
            }
        }
        catch (IOException | URISyntaxException e) {
            logger.info("There was a problem copying the default perspective data.  " +
                                "Details: %s", e.getMessage());
        }
    }

    private static String computeMD5(String name) {
        Md5MessageDigestAlgorithm algorithm = new Md5MessageDigestAlgorithm();
        algorithm.updateWithBytesFromUtf8String(name);
        return algorithm.computeDigestAsBase16Encoding();
    }
}
