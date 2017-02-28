package edu.stanford.bmir.protege.web.server.access;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Jan 2017
 */
public final class ProjectResource implements Resource {

    private ProjectId projectId;

    public ProjectResource(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @Override
    public Optional<ProjectId> getProjectId() {
        return Optional.of(projectId);
    }

    @Override
    public boolean isProject(ProjectId projectId) {
        return this.projectId.equals(projectId);
    }

    @Override
    public boolean isProject() {
        return true;
    }

    @Override
    public boolean isApplication() {
        return false;
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectResource)) {
            return false;
        }
        ProjectResource other = (ProjectResource) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectResource")
                .addValue(projectId)
                .toString();
    }
}
