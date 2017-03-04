package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetCurrentUserInSessionResult;
import edu.stanford.bmir.protege.web.client.dispatch.cache.ResultCache;
import edu.stanford.bmir.protege.web.shared.user.NotSignedInException;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.dispatch.InvocationExceptionTolerantAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
@Singleton
public class DispatchServiceManager {

    @Nonnull
    private final DispatchServiceAsync async;

    @Nonnull
    private final EventBus eventBus;

    @Nonnull
    private final SignInRequiredHandler signInRequiredHandler;

    @Inject
    public DispatchServiceManager(@Nonnull EventBus eventBus,
                                  @Nonnull SignInRequiredHandler signInRequiredHandler) {
        async = GWT.create(DispatchService.class);
        this.eventBus = eventBus;
        this.signInRequiredHandler = signInRequiredHandler;
    }

    private int requestCount;

    private Map<ProjectId, ResultCache> resultCacheMap = new HashMap<ProjectId, ResultCache>();

    private ResultCache getResultCache(ProjectId projectId, EventBus eventBus) {
        ResultCache resultCache = resultCacheMap.get(projectId);
        if(resultCache == null) {
            resultCache = new ResultCache(projectId, eventBus);
            resultCacheMap.put(projectId, resultCache);
        }
        return resultCache;
    }

    @SuppressWarnings("unchecked")
    public <A extends Action<R>, R extends Result> void execute(A action, final DispatchServiceCallback<R> callback) {
        callback.handleSubmittedForExecution();
        if(action instanceof HasProjectId) {
            ProjectId projectId = ((HasProjectId) action).getProjectId();
            ResultCache resultCache = getResultCache(projectId, eventBus);
            Optional<R> result = resultCache.getCachedResult(action);
            if (result.isPresent()) {
                callback.onSuccess(result.get());
                return;
            }
        }
        requestCount++;
        async.executeAction(action, new AsyncCallbackProxy(action, callback));
    }

    public <A extends Action<R>, R extends Result> void execute(A action, final Consumer<R> successConsumer) {
        execute(action, new DispatchServiceCallback<R>() {
            @Override
            public void handleSuccess(R r) {
                successConsumer.accept(r);
            }
        });
    }

    public <A extends Action<R>, R extends Result> void execute(A action, HasBusy hasBusy, final Consumer<R> successConsumer) {
        execute(action, new DispatchServiceCallback<R>() {

            private Timer timer = new Timer() {
                @Override
                public void run() {
                    hasBusy.setBusy(true);
                }
            };

            @Override
            public void handleFinally() {
                hasBusy.setBusy(false);
            }

            @Override
            public void handleSuccess(R r) {
                timer.cancel();
                successConsumer.accept(r);
            }

            @Override
            public void handleSubmittedForExecution() {
                timer.schedule(1000);
            }
        });
    }




    private class AsyncCallbackProxy<R extends Result> implements AsyncCallback<DispatchServiceResultContainer> {

        private Action<?> action;

        private DispatchServiceCallback<Result> delegate;

        public AsyncCallbackProxy(Action<?> action, DispatchServiceCallback<Result> delegate) {
            this.delegate = delegate;
            this.action = action;
        }

        @Override
        public void onFailure(Throwable caught) {
            handleError(caught, action, delegate);
        }

        @Override
        public void onSuccess(DispatchServiceResultContainer result) {
            // TODO: Fix
            if(action instanceof HasProjectId) {
                ResultCache resultCache = getResultCache(((HasProjectId) action).getProjectId(), eventBus);
                resultCache.cacheResult((Action<R>) action, (R) result.getResult());
            }
            // TODO: CACHE RENDERERABLES
//            cacheRenderables(result.getResult());
            dispatchEvents(result.getResult());
            delegate.onSuccess(result.getResult());
        }

    }

//
//    private void cacheRenderables(Object result) {
//        if (result instanceof HasBrowserTextMap) {
//            BrowserTextMap browserTextMap = ((HasBrowserTextMap) result).getBrowserTextMap();
//            renderingManager.registerEntityData(browserTextMap.getOWLEntityData());
//        }
//    }

    private void dispatchEvents(Object result) {
        if(result instanceof HasEventList<?>) {
            EventList<? extends WebProtegeEvent<?>> eventList = ((HasEventList<? extends WebProtegeEvent<?>>) result).getEventList();

            List<? extends WebProtegeEvent<?>> events = eventList.getEvents();
            // TODO: FIX - Should be dispatched by the project event manager otherwise we will get events from the
            // TODO: more than once!

            for(WebProtegeEvent<?> event : events) {
                GWT.log("[DISPATCH] Dispatching event (" + event.toDebugString() + ")");
                if(event.getSource() != null) {
                    eventBus.fireEventFromSource(event.asGWTEvent(), event.getSource());
                }
                else {
                    eventBus.fireEvent(event.asGWTEvent());
                }
            }
        }
    }

    private void handleError(final Throwable throwable, final Action<?> action, final DispatchServiceCallback<?> callback) {
        if(throwable instanceof NotSignedInException) {
            signInRequiredHandler.handleSignInRequired();
            return;
        }
        if (throwable instanceof PermissionDeniedException) {
            // Try to determine if the user is logged in.  The session might have expired.
            execute(new GetCurrentUserInSessionAction(), new DispatchServiceCallback<GetCurrentUserInSessionResult>() {
                @Override
                public void handleSuccess(GetCurrentUserInSessionResult result) {
                    if(result.getUserInSession().getUserDetails().getUserId().isGuest()) {
                        signInRequiredHandler.handleSignInRequired();
                    }
                    else {
                        callback.onFailure(throwable);
                    }
                }

                @Override
                public void handleErrorFinally(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }
        // Skip handling for actions that do not care about errors
        if(action instanceof InvocationExceptionTolerantAction) {
            Optional<String> errorMessage = ((InvocationExceptionTolerantAction) action).handleInvocationException((InvocationException) throwable);
            if(errorMessage.isPresent()) {
                displayAlert(errorMessage.get());
            }
            return;
        }
        callback.onFailure(throwable);
    }

    private void displayAlert(String alert) {
        MessageBox.showAlert(alert);
    }
}
