package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.UIConfigurationConstants;
import edu.stanford.bmir.protege.web.server.WebProtegeProperties;
import org.apache.commons.io.FileUtils;

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

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        File dataDirectory = WebProtegeProperties.getDataDirectory();
        File defaultConfigurationDirectory = new File(dataDirectory, UIConfigurationConstants.DEFAULT_UI_CONFIGURATION_DATA_DIRECTORY_NAME);
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
            throw new WebProtegeConfigurationException("There was a problem copying the default project configurations.  Details: " + e.getMessage());
        }
    }
}
