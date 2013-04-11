package edu.stanford.bmir.protege.web.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.stanford.bmir.protege.web.server.db.mongodb.MongoDBManager;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.smi.protege.util.Log;

public class WebProtegeInitializer implements ServletContextListener {

	
    public void contextInitialized(ServletContextEvent sce) {
        try {
            WebProtegeConfigurationChecker checker = new WebProtegeConfigurationChecker();
            checker.performConfiguration(sce.getServletContext());
        }
        catch (WebProtegeConfigurationException e) {
            WebProtegeWebAppFilter.setConfigError(e);
        }
        catch (Throwable t) {
            WebProtegeWebAppFilter.setError(t);
        }
    }


    public void contextDestroyed(ServletContextEvent sce) {
        MongoDBManager.get().dispose();
        Log.getLogger(WebProtegeInitializer.class).info("WebProtege cleanly disposed");
    }
    
}
