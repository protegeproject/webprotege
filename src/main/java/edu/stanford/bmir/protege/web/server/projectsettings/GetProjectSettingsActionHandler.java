package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_PROJECT_SETTINGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class GetProjectSettingsActionHandler extends AbstractHasProjectActionHandler<GetProjectSettingsAction, GetProjectSettingsResult> {


    @Nonnull
    private final ProjectId projectId;

    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public GetProjectSettingsActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull ProjectId projectId,
                                           ProjectDetailsManager projectDetailsManager) {
        super(accessManager);
        this.projectId = projectId;
        this.projectDetailsManager = projectDetailsManager;
    }

    @Override
    public Class<GetProjectSettingsAction> getActionClass() {
        return GetProjectSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return EDIT_PROJECT_SETTINGS;
    }

    @Override
    public GetProjectSettingsResult execute(GetProjectSettingsAction action, ExecutionContext executionContext) {
        return new GetProjectSettingsResult(projectDetailsManager.getProjectSettings(projectId));
    }

}
