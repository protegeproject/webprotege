package edu.stanford.bmir.protege.web.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.stanford.bmir.protege.web.server.db.mongodb.MongoDBManager;
import edu.stanford.bmir.protege.web.server.filter.WebProtegeWebAppFilter;
import edu.stanford.bmir.protege.web.server.init.*;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LifeCycleListener implements ServletContextListener {

    private List<ConfigurationCheck> configurationChecks = Arrays.asList(new DataDirectoryExistsCheck(), new DataDirectoryReadableAndWritableCheck(), new MongoDBInitializedCheck());


    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initializing WebProtege...");
        ServletContext servletContext = sce.getServletContext();
        WebProtegeFileStore.setup(servletContext);

        String webappRoot = servletContext.getRealPath("/");

        initPaths(webappRoot);

        for(ConfigurationCheck check : configurationChecks) {
            try {
                check.runCheck();
            }
            catch (WebProtegeConfigurationException e) {
                WebProtegeWebAppFilter.setConfigError(e);
                break;
            }
            catch (Exception e) {
                WebProtegeWebAppFilter.setError(e);
                break;
            }
        }

        initProjectManagers();


    }



    public void initProjectManagers() {
        MetaProject metaProject = MetaProjectManager.getManager().getMetaProject();
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
//        ProjectManagerFactory.dispose();
//        MongoDBManager.get().dispose();
        Log.getLogger(LifeCycleListener.class).info("WebProtege cleanly disposed");
    }
}
