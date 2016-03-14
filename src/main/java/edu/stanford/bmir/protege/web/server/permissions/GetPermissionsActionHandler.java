package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetPermissionsResult;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class GetPermissionsActionHandler implements ActionHandler<GetPermissionsAction, GetPermissionsResult> {

    private ProjectPermissionsManager permissionsManager;

    @Inject
    public GetPermissionsActionHandler(ProjectPermissionsManager permissionsManager) {
        this.permissionsManager = checkNotNull(permissionsManager);
    }

    @Override
    public Class<GetPermissionsAction> getActionClass() {
        return GetPermissionsAction.class;
    }

    @Override
    public RequestValidator<GetPermissionsAction> getRequestValidator(GetPermissionsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetPermissionsResult execute(GetPermissionsAction action, ExecutionContext executionContext) {
        return new GetPermissionsResult(permissionsManager.getPermissionsSet(action.getProjectId(), action.getUserId()));
    }
}
