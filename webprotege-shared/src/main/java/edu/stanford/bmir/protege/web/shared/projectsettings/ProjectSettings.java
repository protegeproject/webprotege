package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public class ProjectSettings implements Serializable, IsSerializable {

    private ProjectId projectId;

    private String projectDisplayName;
    
    private String projectDescription;

    private SlackIntegrationSettings slackIntegrationSettings;

    private WebhookSettings webhookSettings;

    /**
     * For serialization purposes only
     */
    private ProjectSettings() {}

    /**
     * Constructs a ProjectSettingsData object.
     * @param projectId The projectId.  Not {@code null}.
     * @param projectDescription The project description. Not {@code null}.
     * @param slackIntegrationSettings
     * @param webhookSettings
     * @throws java.lang.NullPointerException if any parameters are {@code null}.
     */
    public ProjectSettings(@Nonnull ProjectId projectId,
                           @Nonnull String projectDisplayName,
                           @Nonnull String projectDescription,
                           @Nonnull SlackIntegrationSettings slackIntegrationSettings,
                           @Nonnull WebhookSettings webhookSettings) {
        this.projectId = checkNotNull(projectId);
        this.projectDisplayName = checkNotNull(projectDisplayName);
        this.projectDescription = checkNotNull(projectDescription);
        this.slackIntegrationSettings = checkNotNull(slackIntegrationSettings);
        this.webhookSettings = checkNotNull(webhookSettings);
    }

    /**
     * Gets the projectId.
     * @return The projectId.  Not {@code null}.
     */
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the project display name.
     * @return The project display name.  Not {@code null}.
     */
    public String getProjectDisplayName() {
        return projectDisplayName;
    }

    /**
     * Gets the project description.
     * @return The project description as a string.  May be empty. Not {@code null}.
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    public SlackIntegrationSettings getSlackIntegrationSettings() {
        return slackIntegrationSettings;
    }

    public WebhookSettings getWebhookSettings() {
        return webhookSettings;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId,
                                projectDisplayName,
                                projectDescription,
                                slackIntegrationSettings,
                                webhookSettings);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectSettings)) {
            return false;
        }
        ProjectSettings other = (ProjectSettings) obj;
        return this.projectDisplayName.equals(other.projectDisplayName)
                && this.projectDescription.equals(other.projectDescription)
                && this.projectId.equals(other.projectId)
                && this.slackIntegrationSettings.equals(other.slackIntegrationSettings)
                && this.webhookSettings.equals(other.webhookSettings);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ProjectSettings")
                          .addValue(projectId)
                          .add("displayName", projectDisplayName)
                          .add("description", projectDescription)
                          .toString();
    }
}
