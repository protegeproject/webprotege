package edu.stanford.bmir.protege.web.client.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Handles the permissions of a project. It basically keeps a map of the users
 * and their permission on this project.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class ProjectPermissionManager {

    private Project project;
    private Map<String, Collection<String>> user2permissionMap = new HashMap<String, Collection<String>>();

    public ProjectPermissionManager(Project project) {
        this.project = project;
    }

    public boolean hasPermission(String user, String operation) {
        if (user == null) {
            return false; // TODO: might be a too restrictive
        }
        Collection<String> permissions = user2permissionMap.get(user);
        if (permissions == null) {
            return false;
        }
        return permissions.contains(operation); //TODO: what if the permission is not in the metaproject?
    }

    void setUserPermissions(String user, Collection<String> allowedOps) {
        Collection<String> permissions = user2permissionMap.get(user);
        if (permissions == null) {
            permissions = new HashSet<String>();
        }
        permissions.addAll(allowedOps);
        user2permissionMap.put(user, permissions);
    }

    public Project getProject() {
        return project;
    }

    public void dispose() {
        user2permissionMap.clear();
    }

}
