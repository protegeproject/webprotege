package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class GetProjectSettingsResult implements Result {

    private ProjectSettings projectSettings;

    /**
     * For serialization purposes only
     */
    private GetProjectSettingsResult() {
    }

    /**
     * Constructs a GetProjectSettingsResult.
     * @param projectSettings The settings.  Not {@code null}.
     * @throws java.lang.NullPointerException if {@code projectSettings} is {@code null}.
     */
    public GetProjectSettingsResult(ProjectSettings projectSettings) {
        this.projectSettings = checkNotNull(projectSettings);
    }

    /**
     * Gets the {@link edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings}.
     * @return The project settings.  Not {@code null}.
     */
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
        if (!(obj instanceof GetProjectSettingsResult)) {
            return false;
        }
        GetProjectSettingsResult other = (GetProjectSettingsResult) obj;
        return this.projectSettings.equals(other.projectSettings);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("GetProjectSettingsResult")
                .addValue(projectSettings)
                .toString();
    }
}

