package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetCurrentUserInSessionAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetCurrentUserInSessionResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class GetCurrentUserInSessionActionHandler implements ApplicationActionHandler<GetCurrentUserInSessionAction, GetCurrentUserInSessionResult> {

    @Nonnull
    private final UserInSessionFactory userInSessionFactory;

    @Inject
    public GetCurrentUserInSessionActionHandler(@Nonnull UserInSessionFactory userInSessionFactory) {
        this.userInSessionFactory = checkNotNull(userInSessionFactory);
    }

    /**
     * Gets the class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action} handled by this handler.
     * @return The class of {@link edu.stanford.bmir.protege.web.shared.dispatch.Action}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public Class<GetCurrentUserInSessionAction> getActionClass() {
        return GetCurrentUserInSessionAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetCurrentUserInSessionAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetCurrentUserInSessionResult execute(@Nonnull GetCurrentUserInSessionAction action, @Nonnull ExecutionContext executionContext) {
        UserId userId = executionContext.getUserId();
        UserInSession userInSession = userInSessionFactory.getUserInSession(userId);
        return new GetCurrentUserInSessionResult(userInSession);
    }
}
