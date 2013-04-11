package edu.stanford.bmir.protege.web.server.init;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
public interface ConfigurationTask {

    /**
     * Runs this configuration check.  If there is an error and the check fails it throws a {@link WebProtegeConfigurationException}.
     * @throws WebProtegeConfigurationException if the check failed.
     */
    void run() throws WebProtegeConfigurationException;
}
