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
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class GetProjectSettingsActionHandler extends AbstractHasProjectActionHandler<GetProjectSettingsAction, GetProjectSettingsResult> {

    private final ProjectDetailsManager projectDetailsManager;

    private final ValidatorFactory<AdminPermissionValidator> validatorFactory;

    @Inject
    public GetProjectSettingsActionHandler(OWLAPIProjectManager projectManager, ProjectDetailsManager projectDetailsManager, ValidatorFactory<AdminPermissionValidator> validatorFactory) {
        super(projectManager);
        this.projectDetailsManager = projectDetailsManager;
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Class<GetProjectSettingsAction> getActionClass() {
        return GetProjectSettingsAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(GetProjectSettingsAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected GetProjectSettingsResult execute(GetProjectSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        ProjectId projectId = action.getProjectId();
        return new GetProjectSettingsResult(projectDetailsManager.getProjectSettings(projectId));
    }

}
