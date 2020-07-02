package edu.stanford.bmir.protege.web.server.api;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSessionAttribute;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2018
 *
 * An executor for actions that provides the necessary request and execution context.
 */
public class ActionExecutor {

    @Nonnull
    private final DispatchServiceExecutor executor;

    @Inject
    public ActionExecutor(@Nonnull DispatchServiceExecutor executor) {
        this.executor = checkNotNull(executor);
    }

    @SuppressWarnings("unchecked")
    public   <A extends Action<R>,  R extends Result> R execute(A action, UserId userId) {
        try {
            RequestContext requestContext = new RequestContext(userId);
            ExecutionContext executionContext = new ExecutionContext(new Session(userId));
            DispatchServiceResultContainer resultContainer = executor.execute(action, requestContext, executionContext);
            return (R) resultContainer.getResult();
        } catch (ActionExecutionException e) {
            Throwable throwable = e.getCause();
            if(throwable instanceof RuntimeException) {
                throw ((RuntimeException) throwable);
            }
            else {
                throw new InternalServerErrorException();
            }
        }
    }


    private static class Session implements WebProtegeSession {

        private final UserId userId;

        public Session(UserId userId) {
            this.userId = userId;
        }

        @Override
        public <T> Optional<T> getAttribute(WebProtegeSessionAttribute<T> attribute) {
            return Optional.empty();
        }

        @Override
        public <T> void setAttribute(WebProtegeSessionAttribute<T> attribute, T value) {

        }

        @Override
        public void removeAttribute(WebProtegeSessionAttribute<?> attribute) {

        }

        @Override
        public UserId getUserInSession() {
            return userId;
        }

        @Override
        public void setUserInSession(UserId userId) {

        }

        @Override
        public void clearUserInSession() {

        }
    }
}
