package edu.stanford.bmir.protege.web.server;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/03/2013
 */
public class WebProtegeConfigurationException extends RuntimeException {

    public WebProtegeConfigurationException() {
    }

    public WebProtegeConfigurationException(String message) {
        super(message);
    }

    public WebProtegeConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebProtegeConfigurationException(Throwable cause) {
        super(cause);
    }
}
