package edu.stanford.bmir.protege.web.server.sharing;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsResult;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_SHARING_SETTINGS;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public class SetProjectSharingSettingsActionHandler extends AbstractHasProjectActionHandler<SetProjectSharingSettingsAction, SetProjectSharingSettingsResult> {

    private final ProjectSharingSettingsManager sharingSettingsManager;

    @Inject
    public SetProjectSharingSettingsActionHandler(ProjectManager projectManager, ProjectSharingSettingsManager sharingSettingsManager,
                                                  AccessManager accessManager) {
        super(projectManager, accessManager);
        this.sharingSettingsManager = sharingSettingsManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return EDIT_SHARING_SETTINGS;
    }

    @Override
    protected SetProjectSharingSettingsResult execute(SetProjectSharingSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        sharingSettingsManager.setProjectSharingSettings(action.getProjectSharingSettings());
        return new SetProjectSharingSettingsResult();
    }

    @Override
    public Class<SetProjectSharingSettingsAction> getActionClass() {
        return SetProjectSharingSettingsAction.class;
    }
}
