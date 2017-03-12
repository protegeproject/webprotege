package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.inject.ApplicationComponent;
import edu.stanford.bmir.protege.web.server.inject.DaggerApplicationComponent;
import edu.stanford.bmir.protege.web.server.inject.ServletComponent;

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

            servletContext.addServlet("FileDownloadServlet", servletComponent.getFileDownloadServlet())
                          .addMapping("/download");

            servletContext.addServlet("FileUploadServlet", servletComponent.getFileUploadServlet())
                          .addMapping("/webprotege/submitfile");

            servletContext.addListener(applicationComponent.getSessionListener());

            applicationComponent.getWebProtegeConfigurationChecker().performConfiguration(servletContext);

        }
        catch (WebProtegeConfigurationException e) {
            WebProtegeWebAppFilter.setConfigError(e);
        }
        catch (ExceptionInInitializerError error) {
            error.printStackTrace();
            WebProtegeWebAppFilter.setError(error);
        }
        catch (Throwable error) {
            WebProtegeWebAppFilter.setError(error);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
