package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetUIConfigurationAction extends AbstractHasProjectAction<GetUIConfigurationResult> {

    /**
     * For serialization purposes only
     */
    private GetUIConfigurationAction() {
    }

    public GetUIConfigurationAction(ProjectId projectId) {
        super(projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetUIConfigurationAction)) {
            return false;
        }
        GetUIConfigurationAction other = (GetUIConfigurationAction) obj;
        return this.getProjectId().equals(other.getProjectId());
    }

    @Override
    public String toString() {
        return toStringHelper("GetUIConfigurationAction")
                .addValue(getProjectId())
                .toString();
    }
}
