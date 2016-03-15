package edu.stanford.bmir.protege.web.server.user;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserAction;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserResult;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public class LogOutUserActionHandler implements ActionHandler<LogOutUserAction, LogOutUserResult> {

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
        session.clearUserInSession();
        return new LogOutUserResult();
    }
}
