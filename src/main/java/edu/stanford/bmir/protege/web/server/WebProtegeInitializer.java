package edu.stanford.bmir.protege.web.server;

import com.google.inject.CreationException;
import com.google.inject.spi.Message;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.util.Log;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebProtegeInitializer implements ServletContextListener {

    public WebProtegeInitializer() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        try {
            WebProtegeConfigurationChecker checker = WebProtegeInjector.get().getInstance(WebProtegeConfigurationChecker.class);
            checker.performConfiguration(sce.getServletContext());
        }
        catch (WebProtegeConfigurationException e) {
            WebProtegeWebAppFilter.setConfigError(e);
        }
        catch (ExceptionInInitializerError error) {
            error.printStackTrace();
            Throwable rootCause = ExceptionUtils.getRootCause(error);
            if (rootCause instanceof CreationException) {
                for(Message msg : ((CreationException) rootCause.getCause()).getErrorMessages()) {
                    if(msg.getCause() instanceof WebProtegeConfigurationException) {
                        WebProtegeWebAppFilter.setConfigError((WebProtegeConfigurationException) msg.getCause());
                        return;
                    }
                }
            }
            else {
                WebProtegeWebAppFilter.setError(error);
            }
        }
        catch (Throwable error) {
            WebProtegeWebAppFilter.setError(error);
        }
    }


    public void contextDestroyed(ServletContextEvent sce) {
        try {
            MetaProject metaProject = WebProtegeInjector.get().getInstance(MetaProject.class);
            OWLAPIMetaProjectStore.getStore().saveMetaProjectNow(metaProject);
        }
        catch (Throwable e) {
            WebProtegeInjector.get().getInstance(WebProtegeLogger.class).severe(e);
        }
        Log.getLogger(WebProtegeInitializer.class).info("WebProtege cleanly disposed");
    }
}
