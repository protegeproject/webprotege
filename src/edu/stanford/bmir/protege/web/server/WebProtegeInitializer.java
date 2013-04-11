package edu.stanford.bmir.protege.web.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.stanford.bmir.protege.web.server.db.mongodb.MongoDBManager;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.*;
import edu.stanford.smi.protege.util.Log;

import java.util.Arrays;
import java.util.List;

public class WebProtegeInitializer implements ServletContextListener {

    private List<ConfigurationTask> configurationTasks = Arrays.asList(
            new LoadWebProtegeProperties(),
            new CheckWebProtegeDataDirectoryExists(),
            new CheckDataDirectoryIsReadableAndWritable(),
            new CheckMetaProjectExists(),
            new CheckMongoDBConnectionTask());


    public void contextInitialized(ServletContextEvent sce) {
        performConfiguration(sce.getServletContext());
    }


    private boolean performConfiguration(ServletContext servletContext) {
        for(ConfigurationTask task : configurationTasks) {
            try {
                task.run(servletContext);
            }
            catch (WebProtegeConfigurationException e) {
                WebProtegeWebAppFilter.setConfigError(e);
                return false;
            }
            catch (Exception e) {
                WebProtegeWebAppFilter.setError(e);
                return false;
            }
        }
        return true;
    }


    public void contextDestroyed(ServletContextEvent sce) {
        MongoDBManager.get().dispose();
        Log.getLogger(WebProtegeInitializer.class).info("WebProtege cleanly disposed");
    }
}
