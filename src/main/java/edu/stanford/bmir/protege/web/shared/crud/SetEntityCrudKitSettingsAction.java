package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class SetEntityCrudKitSettingsAction implements ProjectAction<SetEntityCrudKitSettingsResult> {

    private ProjectId projectId;

    private EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> fromSettings;

    private EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> toSettings;

    private IRIPrefixUpdateStrategy prefixUpdateStrategy;

    /**
     * For serialization purposes only
     */
    private SetEntityCrudKitSettingsAction() {
    }

    public SetEntityCrudKitSettingsAction(ProjectId projectId, EntityCrudKitSettings<?> fromSettings, EntityCrudKitSettings<?> toSettings, IRIPrefixUpdateStrategy prefixUpdateStrategy) {
        this.projectId = projectId;
        this.toSettings = toSettings;
        this.fromSettings = fromSettings;
        this.prefixUpdateStrategy = prefixUpdateStrategy;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public IRIPrefixUpdateStrategy getPrefixUpdateStrategy() {
        return prefixUpdateStrategy;
    }

    public EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> getToSettings() {
        return toSettings;
    }

    public EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> getFromSettings() {
        return fromSettings;
    }
}
