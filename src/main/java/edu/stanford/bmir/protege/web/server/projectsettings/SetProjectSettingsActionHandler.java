package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectAdminPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsResult;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class SetProjectSettingsActionHandler extends AbstractHasProjectActionHandler<SetProjectSettingsAction, SetProjectSettingsResult> {


    private ProjectSettingsManager projectSettingsManager;

    public SetProjectSettingsActionHandler(ProjectSettingsManager projectSettingsManager) {
        this.projectSettingsManager = projectSettingsManager;
    }

    @Override
    public Class<SetProjectSettingsAction> getActionClass() {
        return SetProjectSettingsAction.class;
    }

    @Override
    protected RequestValidator<SetProjectSettingsAction> getAdditionalRequestValidator(SetProjectSettingsAction action, RequestContext requestContext) {
        return UserHasProjectAdminPermissionValidator.get();
    }

    @Override
    protected SetProjectSettingsResult execute(SetProjectSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        projectSettingsManager.setProjectSettings(action.getProjectSettings());
        return new SetProjectSettingsResult(projectSettingsManager.getProjectSettings(action.getProjectId()));
    }
}
