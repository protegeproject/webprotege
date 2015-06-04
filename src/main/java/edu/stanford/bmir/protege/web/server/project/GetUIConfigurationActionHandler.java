package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.GetUIConfigurationAction;
import edu.stanford.bmir.protege.web.shared.project.GetUIConfigurationResult;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetUIConfigurationActionHandler extends AbstractHasProjectActionHandler<GetUIConfigurationAction, GetUIConfigurationResult> {

    @Inject
    public GetUIConfigurationActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    protected RequestValidator<GetUIConfigurationAction> getAdditionalRequestValidator(GetUIConfigurationAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetUIConfigurationResult execute(GetUIConfigurationAction action, OWLAPIProject project, ExecutionContext executionContext) {
        ProjectLayoutConfiguration configuration = project.getUiConfigurationManager().getProjectLayoutConfiguration(
                executionContext.getUserId()
        );
        return new GetUIConfigurationResult(configuration);
    }

    @Override
    public Class<GetUIConfigurationAction> getActionClass() {
        return GetUIConfigurationAction.class;
    }
}
