package edu.stanford.bmir.protege.web.server.dispatch;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.server.app.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Collections;
import java.util.Locale;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
@ApplicationSingleton
@SuppressWarnings("GwtServiceNotRegistered")
public class DispatchServlet extends WebProtegeRemoteServiceServlet implements DispatchService  {

    @Nonnull
    private final DispatchServiceExecutor executor;

    @Inject
    public DispatchServlet(
            @Nonnull WebProtegeLogger logger,
            @Nonnull DispatchServiceExecutor executor) {
        super(logger);
        this.executor = checkNotNull(executor);
    }

    @Override
    public DispatchServiceResultContainer executeAction(Action action) throws ActionExecutionException, PermissionDeniedException {
        UserId userId = getUserInSession();
        HttpServletRequest request = getThreadLocalRequest();
        var locales = ImmutableList.copyOf(Collections.list(request.getLocales()));
        HttpSession session = request.getSession();
        final RequestContext requestContext = new RequestContext(userId);
        final ExecutionContext executionContext = new ExecutionContext(new WebProtegeSessionImpl(session));
        return executor.execute(action, requestContext, executionContext);
    }
}
