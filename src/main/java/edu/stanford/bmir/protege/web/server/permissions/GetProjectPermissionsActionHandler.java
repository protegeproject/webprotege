package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.access.ProjectResource.forProject;
import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class GetProjectPermissionsActionHandler implements ActionHandler<GetProjectPermissionsAction, GetProjectPermissionsResult> {

    @Nonnull
    private final AccessManager accessManager;

    @Inject
    public GetProjectPermissionsActionHandler(@Nonnull AccessManager accessManager) {
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
                forUser(executionContext.getUserId()),
                forProject(action.getProjectId())
        );
        return new GetProjectPermissionsResult(allowedActions);
    }
}
