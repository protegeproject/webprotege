package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsResult;

import javax.inject.Inject;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class GetProjectPermissionsActionHandler implements ActionHandler<GetProjectPermissionsAction, GetProjectPermissionsResult> {

    private ProjectPermissionsManager permissionsManager;

    private AccessManager accessManager;

    @Inject
    public GetProjectPermissionsActionHandler(ProjectPermissionsManager permissionsManager, AccessManager accessManager) {
        this.permissionsManager = checkNotNull(permissionsManager);
        this.accessManager = checkNotNull(accessManager);
    }

    @Override
    public Class<GetProjectPermissionsAction> getActionClass() {
        return GetProjectPermissionsAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetProjectPermissionsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetProjectPermissionsResult execute(GetProjectPermissionsAction action, ExecutionContext executionContext) {
        Set<ActionId> allowedActions = accessManager.getActionClosure(
                Subject.forUser(executionContext.getUserId()),
                new ProjectResource(action.getProjectId())
        );
        return new GetProjectPermissionsResult(allowedActions);
    }
}
