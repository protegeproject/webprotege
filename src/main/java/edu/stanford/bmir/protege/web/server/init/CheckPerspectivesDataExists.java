package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.perspective.DefaultPerspectiveDataDirectory;
import edu.stanford.bmir.protege.web.shared.auth.Md5MessageDigestAlgorithm;
import org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2013
 */
public class CheckPerspectivesDataExists implements ConfigurationTask {

    private final File defaultPerspectiveDataDirectory;

    @Inject
    public CheckPerspectivesDataExists(@DefaultPerspectiveDataDirectory File defaultPerspectiveDataDirectory) {
        this.defaultPerspectiveDataDirectory = defaultPerspectiveDataDirectory;
    }

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        if(defaultPerspectiveDataDirectory.exists()) {
            return;
        }
        createDefaultPerspectiveDataDirectory(servletContext, defaultPerspectiveDataDirectory);
    }

    private void createDefaultPerspectiveDataDirectory(ServletContext servletContext, File defaultConfigurationDirectory) {
        try {
            File webappRoot = new File(servletContext.getRealPath(""));
            File templateDefaultPerspectiveDataDirectory = new File(webappRoot, "default-perspective-data");
            defaultConfigurationDirectory.getParentFile().mkdirs();
            FileUtils.copyDirectory(templateDefaultPerspectiveDataDirectory, defaultConfigurationDirectory);
            File[] perspectiveFiles = defaultConfigurationDirectory.listFiles();
            if (perspectiveFiles != null) {
                for(File file : perspectiveFiles) {
                    String fileName = file.getName();
                    if(fileName.endsWith(".json") && !fileName.equals("perspective.list.json")) {
                        String strippedFileName = fileName.substring(0, fileName.length() - 5);
                        file.renameTo(new File(defaultConfigurationDirectory, computeMD5(strippedFileName) + ".json"));
                    }
                }
            }
        }
        catch (IOException e) {
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
