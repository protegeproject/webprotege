package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class SetEntityCrudKitSettingsAction implements HasProjectAction<SetEntityCrudKitSettingsResult> {

    private ProjectId projectId;

    private EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> settings;

    /**
     * For serialization purposes only
     */
    private SetEntityCrudKitSettingsAction() {
    }

    public SetEntityCrudKitSettingsAction(ProjectId projectId, EntityCrudKitSettings<?> settings) {
        this.projectId = projectId;
        this.settings = settings;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> getSettings() {
        return settings;
    }
}
