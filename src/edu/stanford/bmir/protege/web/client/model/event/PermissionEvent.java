package edu.stanford.bmir.protege.web.client.model.event;

import java.util.Collection;

import edu.stanford.bmir.protege.web.client.model.Project;

public class PermissionEvent implements SystemEvent {

    private Project project;
    private String user;
    private Collection<String> permissions;

    public PermissionEvent(Project project, String user, Collection<String> permissions) {
        this.project = project;
        this.user = user;
        this.permissions = permissions;
    }

    public Project getProject() {
        return project;
    }

    public String getUser() {
        return user;
    }

    public Collection<String> getPermissions() {
        return permissions;
    }

}
