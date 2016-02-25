package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.perspective.DefaultPerspectiveDataDirectory;
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
        }
        catch (IOException e) {
            throw new WebProtegeConfigurationException("There was a problem copying the default perspective data.  " +
                    "Details: " + e.getMessage());
        }
    }
}
