package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class GetCurrentUserInSessionActionHandler implements ActionHandler<GetCurrentUserInSessionAction, GetCurrentUserInSessionResult> {

    private final UserDetailsManager userDetailsManager;

    private final AccessManager accessManager;

    @Inject
    public GetCurrentUserInSessionActionHandler(UserDetailsManager userDetailsManager,
                                                AccessManager accessManager) {
        this.userDetailsManager = userDetailsManager;
        this.accessManager = accessManager;
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
    public RequestValidator getRequestValidator(GetCurrentUserInSessionAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetCurrentUserInSessionResult execute(GetCurrentUserInSessionAction action, ExecutionContext executionContext) {
        UserId userId = executionContext.getUserId();
        Optional<UserDetails> userDetails = userDetailsManager.getUserDetails(userId);
        UserDetails theUserDetails;
        if (userDetails.isPresent()) {
            theUserDetails = userDetails.get();
        }
        else {
            theUserDetails = UserDetails.getGuestUserDetails();
        }
        return new GetCurrentUserInSessionResult(
                new UserInSession(
                        theUserDetails,
                        accessManager.getActionClosure(Subject.forUser(executionContext.getUserId()),
                                                       ApplicationResource.get())));
    }
}
