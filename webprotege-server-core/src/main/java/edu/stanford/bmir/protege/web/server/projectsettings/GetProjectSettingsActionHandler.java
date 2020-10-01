package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
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
public class GetProjectSettingsActionHandler extends AbstractProjectActionHandler<GetProjectSettingsAction, GetProjectSettingsResult> {


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

    @Nonnull
    @Override
    public Class<GetProjectSettingsAction> getActionClass() {
        return GetProjectSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetProjectSettingsAction action) {
        return EDIT_PROJECT_SETTINGS;
    }

    @Nonnull
    @Override
    public GetProjectSettingsResult execute(@Nonnull GetProjectSettingsAction action, @Nonnull ExecutionContext executionContext) {
        return GetProjectSettingsResult.get(projectDetailsManager.getProjectSettings(projectId));
    }

}
