package edu.stanford.bmir.protege.web.server.init;

import java.io.*;
import java.util.Properties;

import javax.servlet.ServletContext;

import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
public class LoadWebProtegeProperties implements ConfigurationTask {

    private static WebProtegeLogger log = WebProtegeLoggerManager.get(LoadWebProtegeProperties.class);
	/**
     * A prefix for properties specified on the command line or via environment variables
     */
    private static final String SYSTEM_PROPERTY_PREFIX = "webprotege.";


    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
    	Properties properties = new Properties();
    	
    	String fileName = WebProtegeProperties.WEB_PROTEGE_PROPERTIES_FILE_NAME;
    	File file = new File(new File(servletContext.getRealPath("")), fileName);
    	if(file.exists()) {
    		try {
    			InputStream inStream = new BufferedInputStream(new FileInputStream(file));
    			properties.load(inStream);
    			inStream.close();
    		} catch (IOException e) {
    			throw new WebProtegeConfigurationException("Could not read " + file.getAbsolutePath() + ". Message: " + e.getMessage());
    		}
    	}

    	overridePropertiesWithSystemProperties(properties);
    	WebProtegeProperties.initFromProperties(properties);
    }

    /**
     * Overrides any property values in the specified {@link Properties} object with property values that are specified
     * via the command line (with a -D argument) or via environment variables.
     * @param properties The properties object whose property values should be replaced.  Not {@code null}.
     */
    private static void overridePropertiesWithSystemProperties(Properties properties) {
        checkNotNull(properties);
        for(WebProtegePropertyName propertyName : WebProtegePropertyName.values()) {
            String systemPropertyName = SYSTEM_PROPERTY_PREFIX + propertyName.getPropertyName();
            String value = getSystemProperty(systemPropertyName);
            if(value != null) {
                properties.setProperty(propertyName.getPropertyName(), value);
                log.info("WebProtege configuration (using system variable): " + systemPropertyName + " = " + value);
            }
        }
    }
    
    private static String getSystemProperty(String property) {
    	String value = null;
    	try {
    		value = System.getProperty(property, null);
    		if (value == null) {
    			value = System.getenv(property);
    		}
    	} catch (SecurityException e) {
    		log.info("Cannot access system or environment variable: " + property + " Message: " + e.getMessage());
    	}
    	return value;
    }
}
