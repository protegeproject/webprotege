package edu.stanford.bmir.protege.web.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.stanford.bmir.protege.web.server.db.mongodb.MongoDBManager;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectMetadataManager;
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
        try {
            OWLAPIMetaProjectStore.getStore().saveMetaProject(MetaProjectManager.getManager());
        }
        catch (Throwable e) {
            WebProtegeLoggerManager.get(WebProtegeInitializer.class).severe(e);
        }
        try {
            MongoDBManager.get().dispose();
        }
        catch (Throwable e) {
            WebProtegeLoggerManager.get(WebProtegeInitializer.class).severe(e);
        }

        Log.getLogger(WebProtegeInitializer.class).info("WebProtege cleanly disposed");
    }
    
}
