package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionResult;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.server.metaproject.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class GetCurrentUserInSessionActionHandler implements ActionHandler<GetCurrentUserInSessionAction, GetCurrentUserInSessionResult> {

    private final UserDetailsManager userDetailsManager;

    private final ProjectPermissionsManager projectPermissionsManager;

    @Inject
    public GetCurrentUserInSessionActionHandler(UserDetailsManager userDetailsManager, ProjectPermissionsManager projectPermissionsManager) {
        this.userDetailsManager = userDetailsManager;
        this.projectPermissionsManager = projectPermissionsManager;
    }

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
        Optional<UserDetails> userDetails = userDetailsManager.getUserDetails(userId);
        if (userDetails.isPresent()) {
            Set<GroupId> userGroups = projectPermissionsManager.getUserGroups(userId);
            return new GetCurrentUserInSessionResult(userDetails.get(), userGroups);
        }
        else {
            return new GetCurrentUserInSessionResult(UserDetails.getGuestUserDetails(), Collections.<GroupId>emptySet());
        }
    }
}
