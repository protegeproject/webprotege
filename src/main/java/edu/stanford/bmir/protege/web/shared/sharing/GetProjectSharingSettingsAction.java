package edu.stanford.bmir.protege.web.shared.sharing;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public class GetProjectSharingSettingsAction implements ProjectAction<GetProjectSharingSettingsResult> {

    private ProjectId projectId;

    private GetProjectSharingSettingsAction() {
    }

    public GetProjectSharingSettingsAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectSharingSettingsAction)) {
            return false;
        }
        GetProjectSharingSettingsAction other = (GetProjectSharingSettingsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectSharingSettingsAction")
                .addValue(projectId)
                .toString();
    }
}
