package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.db.mongodb.MongoDBManager;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.LoadMailProperties;
import edu.stanford.bmir.protege.web.server.init.LoadWebProtegeProperties;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerEx;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.util.Log;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebProtegeInitializer implements ServletContextListener {

    public static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(WebProtegeInitializer.class);

    public void contextInitialized(ServletContextEvent sce) {
        try {
            LoadWebProtegeProperties loadWebProtegeProperties = new LoadWebProtegeProperties();
            loadWebProtegeProperties.run(sce.getServletContext());

            LoadMailProperties loadMailProperties = new LoadMailProperties();
            loadMailProperties.run(sce.getServletContext());

            WebProtegeConfigurationChecker checker = WebProtegeInjector.get().getInstance(WebProtegeConfigurationChecker.class);
            checker.performConfiguration(sce.getServletContext());
            LOGGER.info("Initialization complete");
            WebProtegeLoggerEx loggerEx = new WebProtegeLoggerEx(LOGGER);
            loggerEx.logMemoryUsage();
        }
        catch (WebProtegeConfigurationException e) {
            LOGGER.severe(e);
            WebProtegeWebAppFilter.setConfigError(e);
        }
        catch (Throwable t) {
            LOGGER.severe(t);
            WebProtegeWebAppFilter.setError(t);
        }
    }


    public void contextDestroyed(ServletContextEvent sce) {
        try {
            OWLAPIMetaProjectStore.getStore().saveMetaProjectNow(MetaProjectManager.getManager().getMetaProject());
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
        MetaProject metaProject = WebProtegeInjector.get().getInstance(MetaProject.class);
        OWLAPIMetaProjectStore.getStore().saveMetaProjectNow(metaProject);
        Log.getLogger(WebProtegeInitializer.class).info("WebProtege cleanly disposed");
    }
}
