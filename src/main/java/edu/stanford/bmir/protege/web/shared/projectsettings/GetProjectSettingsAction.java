package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class GetProjectSettingsAction extends AbstractHasProjectAction<GetProjectSettingsResult> {


    /**
     * For serialization purposes only
     */
    public GetProjectSettingsAction() {
    }

    public GetProjectSettingsAction(ProjectId projectId) {
        super(projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectSettingsAction)) {
            return false;
        }
        GetProjectSettingsAction other = (GetProjectSettingsAction) obj;
        return this.getProjectId().equals(other.getProjectId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId());
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("GetProjectSettingsAction")
                .addValue(getProjectId())
                .toString();
    }
}
