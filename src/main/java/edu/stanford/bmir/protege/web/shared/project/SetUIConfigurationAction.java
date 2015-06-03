package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class SetUIConfigurationAction extends AbstractHasProjectAction<SetUIConfigurationResult> {

    private ProjectId projectId;

    private ProjectLayoutConfiguration configuration;

    /**
     * For serialization purposes only
     */
    private SetUIConfigurationAction() {
    }

    public SetUIConfigurationAction(ProjectId projectId, ProjectLayoutConfiguration configuration) {
        this.projectId = checkNotNull(projectId);
        this.configuration = checkNotNull(configuration);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public ProjectLayoutConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, configuration);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetUIConfigurationAction)) {
            return false;
        }
        SetUIConfigurationAction other = (SetUIConfigurationAction) obj;
        return this.projectId.equals(other.projectId)
                && this.configuration.equals(other.configuration);
    }


    @Override
    public String toString() {
        return toStringHelper("SetUIConfigurationAction")
                .addValue(projectId)
                .addValue(configuration)
                .toString();
    }
}
