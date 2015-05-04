package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class LoadProjectResult implements Result, HasUserId, HasProjectId {

    private ProjectId projectId;

    private UserId userId;

    private ProjectDetails projectDetails;

    private PermissionsSet permissionsSet;

    /**
     * For serialization purposes only
     */
    private LoadProjectResult() {

    }

    public LoadProjectResult(ProjectId projectId, UserId loadedBy, PermissionsSet permissionsSet, ProjectDetails projectDetails) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(loadedBy);
        this.projectDetails = checkNotNull(projectDetails);
        this.permissionsSet = checkNotNull(permissionsSet);
    }

    @Override
    public UserId getUserId() {
        return userId;
    }

    public PermissionsSet getRequestingUserProjectPermissionSet() {
        return permissionsSet;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("LoadProjectResult")
                .addValue(getProjectId())
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId(), userId, projectDetails, permissionsSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LoadProjectResult)) {
            return false;
        }
        LoadProjectResult other = (LoadProjectResult) obj;
        return this.getProjectId().equals(other.getProjectId())
                && this.userId.equals(other.userId)
                && this.projectDetails.equals(other.projectDetails)
                && this.permissionsSet.equals(other.permissionsSet);
    }
}
