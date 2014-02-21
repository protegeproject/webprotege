package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionResult;
import edu.stanford.bmir.protege.web.server.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.Group;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class GetCurrentUserInSessionActionHandler implements ActionHandler<GetCurrentUserInSessionAction, GetCurrentUserInSessionResult> {

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Override
    public Class<GetCurrentUserInSessionAction> getActionClass() {
        return GetCurrentUserInSessionAction.class;
    }

    @Override
    public RequestValidator<GetCurrentUserInSessionAction> getRequestValidator(GetCurrentUserInSessionAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetCurrentUserInSessionResult execute(GetCurrentUserInSessionAction action, ExecutionContext executionContext) {
        UserId userId = executionContext.getUserId();
        final UserDetails userDetails;
        final Set<GroupId> groups = new HashSet<GroupId>();
        if(userId.isGuest()) {
            userDetails = UserDetails.getGuestUserDetails();
        }
        else {
            final MetaProject metaProject = MetaProjectManager.getManager().getMetaProject();
            User user = metaProject.getUser(userId.getUserName());
            for(Group group : user.getGroups()) {
                groups.add(GroupId.get(group.getName()));
            }
            userDetails = UserDetails.getUserDetails(userId, userId.getUserName(), Optional.fromNullable(user.getEmail()));
        }

        return new GetCurrentUserInSessionResult(userDetails, groups);
    }
}
