package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class SetProjectSettingsResult implements Result {

    private ProjectSettings projectSettings;

    private SetProjectSettingsResult() {

    }

    public SetProjectSettingsResult(@Nonnull ProjectSettings projectSettings) {
        this.projectSettings = checkNotNull(projectSettings);
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
        if (!(obj instanceof SetProjectSettingsResult)) {
            return false;
        }
        SetProjectSettingsResult other = (SetProjectSettingsResult) obj;
        return this.getProjectSettings().equals(other.getProjectSettings());
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("SetProjectSettingsResult")
                .addValue(projectSettings)
                .toString();
    }
}
