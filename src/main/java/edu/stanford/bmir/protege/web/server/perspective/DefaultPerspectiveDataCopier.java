package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
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
public class DefaultPerspectiveDataCopier {

    private final File defaultPerspectiveDataDirectory;

    @Inject
    public DefaultPerspectiveDataCopier(@DefaultPerspectiveDataDirectory File defaultPerspectiveDataDirectory) {
        this.defaultPerspectiveDataDirectory = defaultPerspectiveDataDirectory;
    }

    public void copyDefaultPerspectiveData() {
        try {
            URL url = getClass().getResource("/default-perspective-data");
            System.out.println("COPYING DEFAUT PERSPECTIVE DATA: " + url);
            File templateDefaultPerspectiveDataDirectory = new File(url.toURI());
            defaultPerspectiveDataDirectory.getParentFile().mkdirs();
            FileUtils.copyDirectory(templateDefaultPerspectiveDataDirectory, defaultPerspectiveDataDirectory);
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
            throw new WebProtegeConfigurationException("There was a problem copying the default perspective data.  " +
                    "Details: " + e.getMessage());
        }
    }

    private static String computeMD5(String name) {
        Md5MessageDigestAlgorithm algorithm = new Md5MessageDigestAlgorithm();
        algorithm.updateWithBytesFromUtf8String(name);
        return algorithm.computeDigestAsBase16Encoding();
    }
}
