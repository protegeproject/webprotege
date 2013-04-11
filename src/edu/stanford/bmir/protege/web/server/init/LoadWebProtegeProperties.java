package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.WebProtegeProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
public class LoadWebProtegeProperties implements ConfigurationTask {

    @Override
    public void run() throws WebProtegeConfigurationException {
        File file = WebProtegeProperties.WEB_PROTEGE_PROPERTIES_FILE;
        if(!file.exists()) {
            throw new WebProtegeConfigurationException("The WebProtege properties configuration file could not be found.  WebProtege expected to find the file " + file.getAbsolutePath() + " but it could not find it.");
        }
        try {
            Properties properties = new Properties();
            final FileInputStream inStream = new FileInputStream(WebProtegeProperties.WEB_PROTEGE_PROPERTIES_FILE);
            properties.load(inStream);
            inStream.close();
            WebProtegeProperties.initFromProperties(properties);
        }
        catch (IOException e) {
            throw new WebProtegeConfigurationException("Could not read " + WebProtegeProperties.WEB_PROTEGE_PROPERTIES_FILE.getAbsolutePath());
        }
    }
}
