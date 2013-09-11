package edu.stanford.bmir.protege.web.server.crud.persistence;

import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 * <p>
 *     Captures the {@link EntityCrudKitSettings} for a project.
 * </p>
 */
@TypeAlias("ProjectEntityCrudKitSettings")
public class ProjectEntityCrudKitSettings {

    @Id
    private ProjectId projectId;

    private EntityCrudKitSettings<?> settings;

    /**
     * Constructs a {@link ProjectEntityCrudKitSettings}, which specifies the {@link EntityCrudKitSettings} for a
     * specified project.
     * @param projectId The {@link ProjectId} that identifies the settings that apply to the project.  Not {@code null}.
     * @param settings The settings that apply to the project.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public ProjectEntityCrudKitSettings(ProjectId projectId, EntityCrudKitSettings<?> settings) {
        this.projectId = checkNotNull(projectId);
        this.settings = checkNotNull(settings);
    }

    /**
     * Gets the {@link ProjectId}, which specifies the project that these settings apply to.
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the {@link EntityCrudKitSettings} for the project.
     * @return The settings.  Not {@code null}.
     */
    public EntityCrudKitSettings<?> getSettings() {
        return settings;
    }

    @Override
    public int hashCode() {
        return "ProjectEntityCrudKitSettings".hashCode() + projectId.hashCode() + settings.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ProjectEntityCrudKitSettings)) {
            return false;
        }
        ProjectEntityCrudKitSettings other = (ProjectEntityCrudKitSettings) o;
        return this.projectId.equals(other.projectId) && this.settings.equals(other.settings);
    }

}
