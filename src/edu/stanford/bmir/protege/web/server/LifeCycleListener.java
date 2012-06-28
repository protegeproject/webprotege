package edu.stanford.bmir.protege.web.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.stanford.smi.protege.util.Log;

import java.io.File;

public class LifeCycleListener implements ServletContextListener {


    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initializing WebProtege...");
        ServletContext servletContext = sce.getServletContext();
        WebProtegeFileStore.setup(servletContext);
        String webappRoot = servletContext.getRealPath("/");

        initPaths(webappRoot);

        initProjectManagers();
    }



    public void initProjectManagers() {
        ProjectManagerFactory.registerProjectManager(Protege3ProjectManager.getProjectManager());
        //other project managers can be added here
    }

    public void initPaths(String webappRoot) {
        try {
            /*
             * Set the protege.dir to the webapp root,
             * so that protege.properties will be read
             * from the webapp root, rather than tomcat root.
             */
            System.setProperty("protege.dir", webappRoot);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        //this line has to be after setting the system property
        Log.getLogger().info("WebProtege running in: " + webappRoot);
        FileUtil.init(webappRoot); //needed
    }

    public void contextDestroyed(ServletContextEvent sce) {
        // TODO saveAllProjects for LocalMetaProjectManagers?
        ProjectManagerFactory.dispose();
        Log.getLogger(LifeCycleListener.class).info("WebProtege cleanly disposed");
    }
}
