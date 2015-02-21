package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.shared.project.GetUIConfigurationAction;
import edu.stanford.bmir.protege.web.shared.project.GetUIConfigurationResult;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetUIConfigurationActionHandler implements ActionHandler<GetUIConfigurationAction, GetUIConfigurationResult> {

    private UIConfigurationManager uiConfigurationManager;

    @Inject
    public GetUIConfigurationActionHandler(UIConfigurationManager uiConfigurationManager) {
        this.uiConfigurationManager = uiConfigurationManager;
    }

    @Override
    public RequestValidator<GetUIConfigurationAction> getRequestValidator(GetUIConfigurationAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    public GetUIConfigurationResult execute(GetUIConfigurationAction action, ExecutionContext executionContext) {
        ProjectLayoutConfiguration configuration = uiConfigurationManager.getProjectLayoutConfiguration(
                action.getProjectId(),
                executionContext.getUserId()
        );
        return new GetUIConfigurationResult(configuration);
    }

    @Override
    public Class<GetUIConfigurationAction> getActionClass() {
        return GetUIConfigurationAction.class;
    }
}
