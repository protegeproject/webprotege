package edu.stanford.bmir.protege.web.server.dispatch.impl;

import edu.stanford.bmir.protege.web.client.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.server.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public class DispatchServiceImpl extends WebProtegeRemoteServiceServlet implements DispatchService  {

    private DispatchServiceHandler executor = new DefaultDispatchServiceExecutor();

    @Override
    public DispatchServiceResultContainer executeAction(Action<?> action) throws ActionExecutionException, PermissionDeniedException {
        UserId userId = getUserInSession();
        HttpServletRequest request = getThreadLocalRequest();
        HttpSession session = request.getSession();
        final RequestContext requestContext = new RequestContext(userId, session);
        final ExecutionContext executionContext = new ExecutionContext(userId);
        return executor.execute(action, requestContext, executionContext);
    }
}
