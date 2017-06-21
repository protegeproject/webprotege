package edu.stanford.bmir.protege.web.server.sharing;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsResult;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_SHARING_SETTINGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public class GetProjectSharingSettingsActionHandler extends AbstractHasProjectActionHandler<GetProjectSharingSettingsAction, GetProjectSharingSettingsResult> {

    private final ProjectSharingSettingsManager sharingSettingsManager;

    @Inject
    public GetProjectSharingSettingsActionHandler(ProjectSharingSettingsManager sharingSettingsManager, AccessManager accessManager) {
        super(accessManager);
        this.sharingSettingsManager = sharingSettingsManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return EDIT_SHARING_SETTINGS;
    }

    @Override
    public GetProjectSharingSettingsResult execute(GetProjectSharingSettingsAction action, ExecutionContext executionContext) {
        ProjectSharingSettings settings = sharingSettingsManager.getProjectSharingSettings(action.getProjectId());
        return new GetProjectSharingSettingsResult(settings);
    }

    @Override
    public Class<GetProjectSharingSettingsAction> getActionClass() {
        return GetProjectSharingSettingsAction.class;
    }
}
