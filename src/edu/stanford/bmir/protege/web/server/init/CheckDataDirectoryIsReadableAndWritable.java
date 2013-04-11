package edu.stanford.bmir.protege.web.server.init;

import edu.stanford.bmir.protege.web.server.WebProtegeProperties;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.UUID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
public class CheckDataDirectoryIsReadableAndWritable implements ConfigurationTask {

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        File dataDirectory = WebProtegeProperties.getDataDirectory();
        File testFile = new File(dataDirectory, "write.test." + UUID.randomUUID() + ".txt");
        try {
            try {
                OutputStream os = new FileOutputStream(testFile);
                os.write(1);
                os.close();
            }
            catch (FileNotFoundException e) {
                throw new WebProtegeConfigurationException("WebProtege is not able to write to the WebProtege data directory located at " + dataDirectory + ".  Please check that the specified data directory exists and that the user which the servlet container (tomcat) runs under has both read and write permission for this directory and its contents.");
            }
            catch (IOException e) {
                throw new WebProtegeConfigurationException(e);
            }

            try {
                InputStream is = new FileInputStream(testFile);
                is.read();
                is.close();
            }
            catch (FileNotFoundException e) {
                throw new WebProtegeConfigurationException("WebProtege is not able to read the WebProtege data directory located at " + dataDirectory + ".  Please check that the specified data directory exists and that the user which the servlet container (tomcat) runs under has both read and write permission for this directory and its contents.");
            }
            catch (IOException e) {
                throw new WebProtegeConfigurationException(e);
            }
        }
        finally {
            testFile.delete();
        }


    }

}
