package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class SetProjectSettingsAction extends AbstractHasProjectAction<SetProjectSettingsResult> {

    private ProjectSettings projectSettings;

    /**
     * For serialization purposes only
     */
    private SetProjectSettingsAction() {
    }

    public SetProjectSettingsAction(ProjectSettings projectSettings) {
        super(projectSettings.getProjectId());
        this.projectSettings = projectSettings;
    }

    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectSettings);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetProjectSettingsAction)) {
            return false;
        }
        SetProjectSettingsAction other = (SetProjectSettingsAction) obj;
        return this.projectSettings.equals(other.projectSettings);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("SetProjectSettingsAction")
                .addValue(projectSettings)
                .toString();
    }
}
