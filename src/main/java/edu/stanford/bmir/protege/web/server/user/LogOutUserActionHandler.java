package edu.stanford.bmir.protege.web.server.user;

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

    private final UserActivityManager userActivityManager;

    @Inject
    public LogOutUserActionHandler(@Nonnull UserActivityManager userActivityManager) {
        this.userActivityManager = checkNotNull(userActivityManager);
    }

    @Override
    public Class<LogOutUserAction> getActionClass() {
        return LogOutUserAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(LogOutUserAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public LogOutUserResult execute(LogOutUserAction action, ExecutionContext executionContext) {
        WebProtegeSession session = executionContext.getSession();
        UserId userId = session.getUserInSession();
        session.clearUserInSession();
        if (!userId.isGuest()) {
            userActivityManager.setLastLogout(userId, System.currentTimeMillis());
        }
        return new LogOutUserResult();
    }
}
