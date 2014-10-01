package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.db.mongodb.MongoDBManager;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerEx;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.server.reasoning.ReasoningServerManager;
import edu.stanford.protege.reasoning.ReasoningService;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.util.Log;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Set;

public class WebProtegeInitializer implements ServletContextListener {

    public static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(WebProtegeInitializer.class);

    public void contextInitialized(ServletContextEvent sce) {
        try {
            WebProtegeConfigurationChecker checker = new WebProtegeConfigurationChecker();
            checker.performConfiguration(sce.getServletContext());
            warmupMetaProject();
            LOGGER.info("Initialization complete");
            WebProtegeLoggerEx loggerEx = new WebProtegeLoggerEx(LOGGER);
            loggerEx.logMemoryUsage();
        }
        catch (WebProtegeConfigurationException e) {
            LOGGER.info(e.getMessage());
            WebProtegeWebAppFilter.setConfigError(e);
        }
        catch (Throwable t) {
            LOGGER.severe(t);
            WebProtegeWebAppFilter.setError(t);
        }
    }


    public void contextDestroyed(ServletContextEvent sce) {
        try {
            OWLAPIMetaProjectStore.getStore().saveMetaProjectNow(MetaProjectManager.getManager());
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


    private void warmupMetaProject() {
        MetaProject metaProject = LocalMetaProjectManager.getManager().getMetaProject();
        Set<ProjectInstance> projectInstances = metaProject.getProjects();
        int projectInstanceCount = projectInstances.size();
        WebProtegeLoggerManager.get(WebProtegeInitializer.class).info("Loaded meta-project.  There are %d project instances.", projectInstanceCount);
    }

}
