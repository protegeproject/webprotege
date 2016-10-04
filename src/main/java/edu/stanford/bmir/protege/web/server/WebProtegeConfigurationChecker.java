package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.init.ConfigurationTask;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2013
 */
public class WebProtegeConfigurationChecker {

    private final Set<ConfigurationTask> configurationTasks;

    @Inject
    public WebProtegeConfigurationChecker(Set<ConfigurationTask> configurationTasks) {
        this.configurationTasks = configurationTasks;
    }

    public boolean performConfiguration(ServletContext servletContext) throws WebProtegeConfigurationException {

        for(ConfigurationTask task : configurationTasks) {
            task.run(servletContext);
        }

        return true;
    }


}
