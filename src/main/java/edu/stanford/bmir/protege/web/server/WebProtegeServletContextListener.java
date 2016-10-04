package edu.stanford.bmir.protege.web.server;

import com.google.inject.CreationException;
import com.google.inject.spi.Message;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.inject.ApplicationComponent;
import edu.stanford.bmir.protege.web.server.inject.DaggerApplicationComponent;
import edu.stanford.bmir.protege.web.server.inject.ServletComponent;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebProtegeServletContextListener implements ServletContextListener {

    public WebProtegeServletContextListener() {
    }




    public void contextInitialized(ServletContextEvent sce) {
        try {
            ApplicationComponent applicationComponent = DaggerApplicationComponent.create();
            ServletContext servletContext = sce.getServletContext();

            servletContext.setAttribute(ApplicationComponent.class.getName(), applicationComponent);

            ServletComponent servletComponent = applicationComponent.getServletComponent();

            servletContext.addServlet("DispatchService", servletComponent.getDispatchService())
                          .addMapping("/webprotege/dispatchservice");

            servletContext.addServlet("OntologyServiceImpl", servletComponent.getOntologyService())
                          .addMapping("/webprotege/ontology");

            servletContext.addServlet("OBOTextEditorService", servletComponent.getOBOTextEditorService())
                          .addMapping("/webprotege/obotexteditorservice");

            servletContext.addServlet("BioPortalAPIService", servletComponent.getBioPortalAPIService())
                          .addMapping("/webprotege/bioportalapi");

            servletContext.addServlet("FileDownloadServlet", servletComponent.getFileDownloadServlet())
                          .addMapping("/download");

            servletContext.addServlet("FileUploadServlet", servletComponent.getFileUploadServlet())
                          .addMapping("/webprotege/submitfile");


            applicationComponent.getWebProtegeConfigurationChecker().performConfiguration(servletContext);

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
