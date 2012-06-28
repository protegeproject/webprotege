package edu.stanford.bmir.protege.web.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import edu.stanford.bmir.protegex.chao.ChAOKbManager;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.OntologyComponentFactory;
import edu.stanford.smi.protege.collab.util.ChAOCacheUpdater;
import edu.stanford.smi.protege.collab.util.HasAnnotationCache;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.server_changes.ChangesProject;

/**
 * Main class for managing Protege 3 projects on the server side. It has support for:
 * <ul>
 * <li> loading a local or remote project</li>
 * <li> get the remote Protege server </li>
 * <li> caches opened projects </li>
 * <li> has a thread for automatically saving local projects at a set interval </li>
 * </ul>
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class Protege3ProjectManager extends AbstractProjectManager implements ProjectManager {

    private static Protege3ProjectManager projectManager;
    private MetaProjectManager metaProjectManager;

    private final static Map<String, ServerProject<Project>> openProjectsMap = new HashMap<String, ServerProject<Project>>();

    private Protege3ProjectManager() {
       if (!ApplicationProperties.getLoadOntologiesFromServer()) {
            metaProjectManager = new LocalMetaProjectManager();
            Log.getLogger().info("WebProtege server running with local projects");
        } else { //load ontologies from server
            metaProjectManager = new RemoteMetaProjectManager();
            Log.getLogger().info("WebProtege server running with remote projects loaded" +
            		" from the Protege server: " + getProtegeServerHostName());
        }
    }

    public static Protege3ProjectManager getProjectManager() {
        synchronized (openProjectsMap) {
            if (projectManager == null) {
                projectManager = new Protege3ProjectManager();
            }
            return projectManager;
        }
    }


    /*
      * Project management methods
      */

    public MetaProjectManager getMetaProjectManager() {
        return metaProjectManager;
    }

    public Project getProject(String projectName) {
        return getProject(projectName, true);
    }

    private Project getProject(String projectName, boolean create) {
        ServerProject<Project> serverProject = getServerProject(projectName);
        return (serverProject == null && create) ? null : serverProject.getProject();
    }

    public ServerProject<Project> getServerProject(String projectName) {
        return getServerProject(projectName, true);
    }

    //not the protege server project, but the webprotege server project, which can be local (confusing, no?)

    public ServerProject<Project> getServerProject(String projectName, boolean create) {
        ServerProject<Project> serverProject;
        synchronized (openProjectsMap) {
            serverProject = openProjectsMap.get(projectName);
            if (serverProject == null && create) {
                serverProject = new ServerProject<Project>();
                openProjectsMap.put(projectName, serverProject);
            }
        }
        if (create) {
            ensureProjectOpen(projectName, serverProject);
        }
        return serverProject;
    }

    private void ensureProjectOpen(String projectName, ServerProject<Project> serverProject) {
        synchronized (serverProject) {
            if (serverProject.getProject() == null) {
                Project project = metaProjectManager.openProject(projectName);
                if (project == null) {
                    throw new RuntimeException("Cannot open project " + projectName);
                }
                //load also ChAO KB if available
                KnowledgeBase kb = project.getKnowledgeBase();
                final KnowledgeBase chaoKb = ChAOKbManager.getChAOKb(kb);
                //TODO: check this: it will start change tracking, even if not enabled
                if (!ChangesProject.isInitialized(project)) { //TODO: not the ideal solution. Should be in changes project code
                    ChangesProject.initialize(project);
                }

                HasAnnotationCache.fillHasAnnotationCache(kb);
                //FIXME: this needs to be clean up when the client project closes
                // TODO: MH - I've added this not null check.  The method in attachChAOKbListener in ChAOKbManager
                // TODO: doesn't check for null and can result in a null pointer exception.
                if (ChAOKbManager.getChAOKb(kb) != null) {
                    ChAOCacheUpdater chaoCacheUpdater = new ChAOCacheUpdater(kb);
                    chaoCacheUpdater.initialize();
                }

                serverProject.setProject(project);
                serverProject.setProjectName(projectName);

                AnnotationCache.updateAnnotationCache(project);
                WatchedEntitiesCache.init(project, chaoKb != null ? new OntologyComponentFactory(chaoKb) : null);
            }
        }
    }


    public String getProtegeServerHostName() {
        return ApplicationProperties.getProtegeServerHostName();
    }

     Collection<ServerProject<Project>> getOpenServerProjects() {
         synchronized (openProjectsMap) {
             return new HashSet<ServerProject<Project>>(openProjectsMap.values());
        }
    }

    ServerProject<Project> getOpenedServerProject(String name) {
        synchronized (openProjectsMap) {
            return openProjectsMap.get(name);
        }
    }

    ServerProject<Project> removeServerProject(String name) {
        ServerProject<Project> serverProject = null;
        synchronized (openProjectsMap) {
            serverProject = openProjectsMap.remove(name);
        }
        return serverProject;
    }


    public void dispose() {
        synchronized (openProjectsMap) {
            for (ServerProject<Project> sproject : openProjectsMap.values()) {
                sproject.dispose();
            }
            openProjectsMap.clear();
            if (projectManager != null) {
                projectManager.getMetaProjectManager().dispose();
                projectManager = null;
            }
        }
    }


}
