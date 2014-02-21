package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.app.App;
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

    /**
     * A list of initialization tasks.  Ordering should be unimportant.
     */
    private List<ConfigurationTask> configurationTasks = Arrays.asList(
            new CheckWebProtegeDataDirectoryExists(),
            new CheckDataDirectoryIsReadableAndWritable(),
            new CheckMetaProjectExists(),
            new CheckUIConfigurationDataExists(),
            new CheckMongoDBConnectionTask()
    );

    public boolean performConfiguration(ServletContext servletContext) throws WebProtegeConfigurationException {

        LoadWebProtegeProperties loadWebProtegeProperties = new LoadWebProtegeProperties();
        loadWebProtegeProperties.run(servletContext);

        LoadMailProperties loadMailProperties = new LoadMailProperties();
        loadMailProperties.run(servletContext);

        for(ConfigurationTask task : configurationTasks) {
            task.run(servletContext);
        }

        return true;
    }


}
