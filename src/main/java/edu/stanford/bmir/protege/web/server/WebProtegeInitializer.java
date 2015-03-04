package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerEx;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.util.Log;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebProtegeInitializer implements ServletContextListener {

    public final WebProtegeLogger logger;

    public WebProtegeInitializer() {
        logger = WebProtegeInjector.get().getInstance(WebProtegeLogger.class);
    }

    public void contextInitialized(ServletContextEvent sce) {
        try {
//            LoadWebProtegeProperties loadWebProtegeProperties = new LoadWebProtegeProperties();
//            loadWebProtegeProperties.run(sce.getServletContext());

            WebProtegeConfigurationChecker checker = WebProtegeInjector.get().getInstance(WebProtegeConfigurationChecker.class);
            checker.performConfiguration(sce.getServletContext());

            logger.info("Initialization complete");
            WebProtegeLoggerEx loggerEx = new WebProtegeLoggerEx(logger);
            loggerEx.logMemoryUsage();
        }
        catch (WebProtegeConfigurationException e) {
            logger.severe(e);
            WebProtegeWebAppFilter.setConfigError(e);
        }
        catch (Throwable t) {
            logger.severe(t);
            WebProtegeWebAppFilter.setError(t);
        }
    }


    public void contextDestroyed(ServletContextEvent sce) {
        try {
            OWLAPIMetaProjectStore.getStore().saveMetaProjectNow(MetaProjectManager.getManager().getMetaProject());
        }
        catch (Throwable e) {
            logger.severe(e);
        }
        MetaProject metaProject = WebProtegeInjector.get().getInstance(MetaProject.class);
        OWLAPIMetaProjectStore.getStore().saveMetaProjectNow(metaProject);
        Log.getLogger(WebProtegeInitializer.class).info("WebProtege cleanly disposed");
    }
}
