package edu.stanford.bmir.protege.web.server;

import com.google.inject.CreationException;
import com.google.inject.spi.Message;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
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

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
