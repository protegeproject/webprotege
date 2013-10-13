package edu.stanford.bmir.protege.web.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.stanford.bmir.protege.web.server.db.mongodb.MongoDBManager;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectMetadataManager;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.util.Log;

import java.util.Set;

public class WebProtegeInitializer implements ServletContextListener {

	
    public void contextInitialized(ServletContextEvent sce) {
        try {
            WebProtegeConfigurationChecker checker = new WebProtegeConfigurationChecker();
            checker.performConfiguration(sce.getServletContext());
            warmupMetaProject();
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


    private void warmupMetaProject() {
        MetaProject metaProject = LocalMetaProjectManager.getManager().getMetaProject();
        Set<ProjectInstance> projectInstances = metaProject.getProjects();
        int projectInstanceCount = projectInstances.size();
        WebProtegeLoggerManager.get(WebProtegeInitializer.class).info("Loaded meta-project.  There are %d project instances.", projectInstanceCount);
    }

}
