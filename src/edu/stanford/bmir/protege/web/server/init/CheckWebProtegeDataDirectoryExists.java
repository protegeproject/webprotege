package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.WebProtegeProperties;

import java.io.File;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
public class CheckWebProtegeDataDirectoryExists implements ConfigurationTask {

    @Override
    public void run() throws WebProtegeConfigurationException {
       File dataDirectory = WebProtegeProperties.WEB_PROTEGE_PROPERTIES_FILE;
       if(!dataDirectory.exists()) {
           throw new WebProtegeConfigurationException(getDataDirectoryDoesNotExistsMessage(dataDirectory));
       }
    }

    public static String getDataDirectoryDoesNotExistsMessage(File dataDirectory) {
        return "The WebProtege data directory cannot be found or WebProtege does not have permission to read this directory.  WebProtege expected the data directory to be located at " + dataDirectory.getAbsolutePath() + ".  Please check that the specified data directory exists and that the user which the servlet container (tomcat) runs under has read permission for this directory and its contents.";
    }
}
