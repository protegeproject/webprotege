package edu.stanford.bmir.protege.web.server.crud.persistence;

import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.springframework.data.annotation.Id;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 * <p>
 *     Captures the {@link EntityCrudKitSettings} for a project.
 * </p>
 */
public class ProjectEntityCrudKitSettings {

    @Id
    private ProjectId projectId;

    private EntityCrudKitSettings<?> settings;

    public ProjectEntityCrudKitSettings(ProjectId projectId, EntityCrudKitSettings<?> settings) {
        this.projectId = projectId;
        this.settings = settings;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public EntityCrudKitSettings<?> getSettings() {
        return settings;
    }
}
