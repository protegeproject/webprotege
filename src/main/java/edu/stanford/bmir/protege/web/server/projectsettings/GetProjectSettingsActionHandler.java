package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class GetProjectSettingsActionHandler extends AbstractHasProjectActionHandler<GetProjectSettingsAction, GetProjectSettingsResult> {

    private ProjectDetailsManager projectDetailsManager;

    @Inject
    public GetProjectSettingsActionHandler(OWLAPIProjectManager projectManager, ProjectDetailsManager projectDetailsManager) {
        super(projectManager);
        this.projectDetailsManager = projectDetailsManager;
    }

    @Override
    public Class<GetProjectSettingsAction> getActionClass() {
        return GetProjectSettingsAction.class;
    }

    @Override
    protected RequestValidator<GetProjectSettingsAction> getAdditionalRequestValidator(GetProjectSettingsAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetProjectSettingsResult execute(GetProjectSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        ProjectId projectId = action.getProjectId();
        return new GetProjectSettingsResult(projectDetailsManager.getProjectSettings(projectId));
    }

}
