package edu.stanford.bmir.protege.web.server;

import com.google.inject.CreationException;
import com.google.inject.spi.Message;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.inject.ApplicationComponent;
import edu.stanford.bmir.protege.web.server.inject.DaggerApplicationComponent;
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

            servletContext.addServlet("DispatchService", applicationComponent.getDispatchService())
                          .addMapping("/webprotege/dispatchservice");

            servletContext.addServlet("OntologyServiceImpl", applicationComponent.getOntologyService())
                          .addMapping("/webprotege/ontology");

            servletContext.addServlet("OBOTextEditorService", applicationComponent.getOBOTextEditorService())
                          .addMapping("/webprotege/obotexteditorservice");

            servletContext.addServlet("BioPortalAPIService", applicationComponent.getBioPortalAPIService())
                          .addMapping("/webprotege/bioportalapi");

            servletContext.addServlet("FileDownloadServlet", applicationComponent.getFileDownloadServlet())
                          .addMapping("/download");

            servletContext.addServlet("FileUploadServlet", applicationComponent.getFileUploadServlet())
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
