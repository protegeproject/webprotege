package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.AdminPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsResult;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class SetProjectSettingsActionHandler extends AbstractHasProjectActionHandler<SetProjectSettingsAction, SetProjectSettingsResult> {

    private final ProjectDetailsManager projectDetailsManager;

    private final ValidatorFactory<AdminPermissionValidator> validatorFactory;

    @Inject
    public SetProjectSettingsActionHandler(OWLAPIProjectManager projectManager, ProjectDetailsManager projectDetailsManager, ValidatorFactory<AdminPermissionValidator> validatorFactory) {
        super(projectManager);
        this.projectDetailsManager = projectDetailsManager;
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Class<SetProjectSettingsAction> getActionClass() {
        return SetProjectSettingsAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(SetProjectSettingsAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected SetProjectSettingsResult execute(SetProjectSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        projectDetailsManager.setProjectSettings(action.getProjectSettings());
        return new SetProjectSettingsResult(projectDetailsManager.getProjectSettings(action.getProjectId()));
    }
}
