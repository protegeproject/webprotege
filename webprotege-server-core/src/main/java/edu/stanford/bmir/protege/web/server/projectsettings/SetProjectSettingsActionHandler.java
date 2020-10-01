package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_PROJECT_SETTINGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class SetProjectSettingsActionHandler extends AbstractProjectActionHandler<SetProjectSettingsAction, SetProjectSettingsResult> {

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public SetProjectSettingsActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull ProjectDetailsManager projectDetailsManager) {
        super(accessManager);
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<SetProjectSettingsAction> getActionClass() {
        return SetProjectSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetProjectSettingsAction action) {
        return EDIT_PROJECT_SETTINGS;
    }

    @Nonnull
    @Override
    public SetProjectSettingsResult execute(@Nonnull SetProjectSettingsAction action, @Nonnull ExecutionContext executionContext) {
        projectDetailsManager.setProjectSettings(action.getProjectSettings());
        return new SetProjectSettingsResult(projectDetailsManager.getProjectSettings(action.getProjectId()));
    }
}
