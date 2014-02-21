package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class ProjectCreatedEvent extends Event<ProjectCreatedHandler> implements HasProjectId {

    public static final Type<ProjectCreatedHandler> TYPE = new Type<ProjectCreatedHandler>();

    private ProjectDetails projectDetails;

    private ProjectCreatedEvent() {
    }

    public ProjectCreatedEvent(ProjectDetails projectDetails) {
        this.projectDetails = projectDetails;
    }


    public ProjectId getProjectId() {
        return projectDetails.getProjectId();
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    @Override
    public Type<ProjectCreatedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ProjectCreatedHandler handler) {
        handler.handleProjectCreated(this);
    }

    @Override
    public int hashCode() {
        return "ProjectCreatedEvent".hashCode() + projectDetails.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectCreatedEvent)) {
            return false;
        }
        ProjectCreatedEvent other = (ProjectCreatedEvent) obj;
        return this.projectDetails.equals(other.projectDetails);
    }
}
