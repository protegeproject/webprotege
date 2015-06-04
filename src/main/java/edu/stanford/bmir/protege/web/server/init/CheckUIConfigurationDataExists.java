package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.inject.DefaultUiConfigurationDirectory;
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
public class CheckUIConfigurationDataExists implements ConfigurationTask {

    private final File defaultConfigurationDirectory;

    @Inject
    public CheckUIConfigurationDataExists(@DefaultUiConfigurationDirectory File defaultConfigurationDirectory) {
        this.defaultConfigurationDirectory = defaultConfigurationDirectory;
    }

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        if(defaultConfigurationDirectory.exists()) {
            return;
        }
        createDefaultConfigurationData(servletContext, defaultConfigurationDirectory);
    }

    private void createDefaultConfigurationData(ServletContext servletContext, File defaultConfigurationDirectory) {
        try {
            File webappRoot = new File(servletContext.getRealPath(""));
            File templateDefaultConfigurationsDirectory = new File(webappRoot, "default-configurations");
            defaultConfigurationDirectory.getParentFile().mkdirs();
            FileUtils.copyDirectory(templateDefaultConfigurationsDirectory, defaultConfigurationDirectory);
        }
        catch (IOException e) {
            throw new WebProtegeConfigurationException("There was a problem copying the default project configurations.  " +
                    "Details: " + e.getMessage());
        }
    }
}
