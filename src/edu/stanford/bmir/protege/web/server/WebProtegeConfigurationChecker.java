package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.init.*;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2013
 */
public class WebProtegeConfigurationChecker {

    private List<ConfigurationTask> configurationTasks = Arrays.asList(
            new LoadWebProtegeProperties(),
            new CheckWebProtegeDataDirectoryExists(),
            new CheckDataDirectoryIsReadableAndWritable(),
            new CheckMetaProjectExists(),
            new CheckUIConfigurationDataExists(),
            new CheckMongoDBConnectionTask()
    );

    public boolean performConfiguration(ServletContext servletContext) throws WebProtegeConfigurationException {
        for(ConfigurationTask task : configurationTasks) {
            task.run(servletContext);
        }
        return true;
    }


}
