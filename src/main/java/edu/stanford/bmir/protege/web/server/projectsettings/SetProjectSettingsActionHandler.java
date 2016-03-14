package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectAdminPermissionValidator;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsResult;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class SetProjectSettingsActionHandler extends AbstractHasProjectActionHandler<SetProjectSettingsAction, SetProjectSettingsResult> {


    private ProjectDetailsManager projectDetailsManager;

    @Inject
    public SetProjectSettingsActionHandler(ProjectDetailsManager projectDetailsManager, OWLAPIProjectManager projectManager) {
        super(projectManager);
        this.projectDetailsManager = projectDetailsManager;
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
        projectDetailsManager.setProjectSettings(action.getProjectSettings());
        return new SetProjectSettingsResult(projectDetailsManager.getProjectSettings(action.getProjectId()));
    }
}
