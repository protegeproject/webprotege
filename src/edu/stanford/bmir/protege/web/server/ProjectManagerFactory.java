package edu.stanford.bmir.protege.web.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds a collection of ProjectManagers. A suitable ProjectManager can be
 * retrieved for a certain project name. This allows the implementation of different
 * ProjectManagers for different backends, e.g., the Protege 3 and OWL-API.
 * Based on a project name, the caller will retrieve a project manager that can
 * deal with that particular backend.
 * The class has methods for registering and de-registering project managers.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class ProjectManagerFactory {
    //TODO: Talk to Tim or Jack about synchronization in the methods
    private static Collection<ProjectManager> prjManagers = new ArrayList<ProjectManager>();

    private static Map<String, ProjectManager> name2prjManager = new HashMap<String, ProjectManager>();

    public static ProjectManager getProjectManager(String prjName) {
        ProjectManager prjManager = name2prjManager.get(prjName);
        if (name2prjManager.containsKey(prjName)) {
            return prjManager;
        }
        prjManager = selectProjectManager(prjName);
        name2prjManager.put(prjName, prjManager);
        return prjManager;
    }

    public static Protege3ProjectManager getProtege3ProjectManager() {
        return Protege3ProjectManager.getProjectManager();
    }

    private static ProjectManager selectProjectManager(String prjName) {
        for (ProjectManager prjManager : prjManagers) {
            if (prjManager.isSuitable(prjName)) {
                return prjManager;
            }
        }
        return null;
    }

    public static void registerProjectManager(ProjectManager projectManager) {
        prjManagers.add(projectManager);
    }

    public static void deregisterProjectManager(ProjectManager projectManager) {
        prjManagers.remove(projectManager);
    }

    public static void dispose() {
        for (ProjectManager prjManager : prjManagers) {
            prjManager.dispose();
        }
    }
}
