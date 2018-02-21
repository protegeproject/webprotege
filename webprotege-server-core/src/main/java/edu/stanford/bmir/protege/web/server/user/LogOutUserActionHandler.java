package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserAction;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public class LogOutUserActionHandler implements ApplicationActionHandler<LogOutUserAction, LogOutUserResult> {

    @Nonnull
    private final UserActivityManager userActivityManager;

    @Nonnull
    private final UserInSessionFactory userInSessionFactory;

    @Inject
    public LogOutUserActionHandler(@Nonnull UserActivityManager userActivityManager,
                                   @Nonnull UserInSessionFactory userInSessionFactory) {
        this.userActivityManager = checkNotNull(userActivityManager);
        this.userInSessionFactory = checkNotNull(userInSessionFactory);
    }

    @Nonnull
    @Override
    public Class<LogOutUserAction> getActionClass() {
        return LogOutUserAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull LogOutUserAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public LogOutUserResult execute(@Nonnull LogOutUserAction action, @Nonnull ExecutionContext executionContext) {
        WebProtegeSession session = executionContext.getSession();
        UserId userId = session.getUserInSession();
        session.clearUserInSession();
        if (!userId.isGuest()) {
            userActivityManager.setLastLogout(userId, System.currentTimeMillis());
        }
        return new LogOutUserResult(userInSessionFactory.getUserInSession(UserId.getGuest()));
    }
}
