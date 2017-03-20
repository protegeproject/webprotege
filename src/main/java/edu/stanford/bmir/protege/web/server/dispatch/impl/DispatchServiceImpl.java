package edu.stanford.bmir.protege.web.server.dispatch.impl;

import edu.stanford.bmir.protege.web.client.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.server.app.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
@SuppressWarnings("GwtServiceNotRegistered")
public class DispatchServiceImpl extends WebProtegeRemoteServiceServlet implements DispatchService  {

    @Nonnull
    private final DispatchServiceExecutor executor;

    @Inject
    public DispatchServiceImpl(
            @Nonnull WebProtegeLogger logger,
            @Nonnull DispatchServiceExecutor executor) {
        super(logger);
        this.executor = checkNotNull(executor);
    }

    @Override
    public DispatchServiceResultContainer executeAction(Action action) throws ActionExecutionException, PermissionDeniedException {
        UserId userId = getUserInSession();
        HttpServletRequest request = getThreadLocalRequest();
        HttpSession session = request.getSession();
        final RequestContext requestContext = new RequestContext(userId, session);
        final ExecutionContext executionContext = new ExecutionContext(new WebProtegeSessionImpl(session));
        return executor.execute(action, requestContext, executionContext);
    }
}
