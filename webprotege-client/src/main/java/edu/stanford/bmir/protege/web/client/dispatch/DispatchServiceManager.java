package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.cache.ResultCache;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.user.LoggedInUser;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.TimeUtil;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.dispatch.*;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import elemental.client.Browser;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
@ApplicationSingleton
public class DispatchServiceManager {

    @Nonnull
    private final DispatchServiceAsync async;

    @Nonnull
    private final EventBus eventBus;

    @Nonnull
    private final SignInRequiredHandler signInRequiredHandler;

    @Nonnull
    private final LoggedInUser loggedInUser;

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final MessageBox messageBox;

    private DispatchErrorMessageDisplay errorDisplay;

    private int batch = 0;

    private List<PendingActionExecution<?,?>> pendingActionExecutions = new ArrayList<>();

    @Inject
    public DispatchServiceManager(@Nonnull EventBus eventBus,
                                  @Nonnull SignInRequiredHandler signInRequiredHandler,
                                  @Nonnull LoggedInUser loggedInUser,
                                  @Nonnull PlaceController placeController,
                                  @Nonnull MessageBox messageBox, DispatchErrorMessageDisplay errorDisplay) {
        this.loggedInUser = checkNotNull(loggedInUser);
        this.placeController = placeController;
        this.messageBox = messageBox;
        this.errorDisplay = errorDisplay;
        async = GWT.create(DispatchService.class);
        this.eventBus = checkNotNull(eventBus);
        this.signInRequiredHandler = checkNotNull(signInRequiredHandler);
    }

    private int requestCount;

    private Map<ProjectId, ResultCache> resultCacheMap = new HashMap<>();

    public void beginBatch() {
        batch++;
    }

    public void executeCurrentBatch() {
        batch--;
        if(batch != 0) {
            // Still in an outer batch
            return;
        }
        ImmutableList<PendingActionExecution<?, ?>> pending = ImmutableList.copyOf(pendingActionExecutions);
        pendingActionExecutions.clear();
        ImmutableList.Builder<Action<?>> builder = ImmutableList.builder();
        for(PendingActionExecution<?,?> execution : pending) {
            Action<?> action = execution.getAction();
            builder.add(action);
        }
        BatchAction batchAction = BatchAction.create(builder.build());
        execAction(batchAction, new BatchActionCallback(errorDisplay, pending));
    }

    private ResultCache getResultCache(ProjectId projectId, EventBus eventBus) {
        ResultCache resultCache = resultCacheMap.get(projectId);
        if(resultCache == null) {
            resultCache = new ResultCache(projectId, eventBus);
            resultCacheMap.put(projectId, resultCache);
        }
        return resultCache;
    }

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
        if(batch > 0) {
            GWT.log("[Dispatch]     Batching submitted action: " + action.getClass().getSimpleName());
            AsyncCallbackProxy<R> proxy = new AsyncCallbackProxy(action, callback);
            PendingActionExecution<A, R> actionExecution = PendingActionExecution.get(action, proxy);
            pendingActionExecutions.add(actionExecution);
        }
        else {
            execAction(action, callback);
        }
    }

    @SuppressWarnings("unchecked")
    private <A extends Action<R>, R extends Result> void execAction(A action, DispatchServiceCallback<R> callback) {
        requestCount++;
        logAction(action);
        async.executeAction(action, new AsyncCallbackProxy(action, callback));
    }

    private <A extends Action<R>, R extends Result> void logAction(A action) {
        if ((action instanceof GetProjectEventsAction)) {
            return;
        }
        if(action instanceof BatchAction) {
            GWT.log("[Dispatch] Executing action " + requestCount + "    " + action.getClass().getSimpleName() + "(" + ((BatchAction) action).getActions().size() + " actions)");
        }
        else {
            GWT.log("[Dispatch] Executing action " + requestCount + "    " + action.getClass().getSimpleName());
        }
    }


    public <A extends Action<R>, R extends Result> void execute(A action, final Consumer<R> successConsumer) {
        execute(action, new DispatchServiceCallback<R>(errorDisplay) {
            @Override
            public void handleSuccess(R r) {
                successConsumer.accept(r);
            }
        });
    }

    public <A extends Action<R>, R extends Result> void execute(A action, HasBusy hasBusy, final Consumer<R> successConsumer) {
        execute(action, new DispatchServiceCallback<R>(errorDisplay) {

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

        private Stopwatch stopwatch = Stopwatch.createUnstarted();

        public AsyncCallbackProxy(Action<?> action, DispatchServiceCallback<Result> delegate) {
            this.delegate = delegate;
            this.action = action;
            stopwatch.start();
        }

        @Override
        public void onFailure(Throwable caught) {
            handleError(caught, action, delegate);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onSuccess(DispatchServiceResultContainer result) {
            stopwatch.stop();
            long ellapsedTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            if(ellapsedTime > 200) {
                GWT.log("[Dispatch] Elapsed time for " + action.getClass().getSimpleName() + " is " + ellapsedTime + "ms");
            }
            if(action instanceof HasProjectId) {
                ResultCache resultCache = getResultCache(((HasProjectId) action).getProjectId(), eventBus);
                resultCache.cacheResult((Action<R>) action, (R) result.getResult());
            }
            dispatchEvents(result.getResult());
            delegate.onSuccess(result.getResult());
        }
    }

    private void dispatchEvents(Object result) {
        if(result instanceof HasEventList<?>) {
            EventList<? extends WebProtegeEvent<?>> eventList = ((HasEventList<? extends WebProtegeEvent<?>>) result).getEventList();

            List<? extends WebProtegeEvent<?>> events = eventList.getEvents();
            // TODO: FIX - Should be dispatched by the project event manager otherwise we will get events from the
            // TODO: more than once!
            GWT.log("[Dispatch] Dispatching " + events.size() + " events");
            long t0 = TimeUtil.getCurrentTime();
            for(WebProtegeEvent<?> event : events) {
                GWT.log("[Dispatch] Dispatching event (" + event + ")");
                if(event.getSource() != null) {
                    eventBus.fireEventFromSource(event.asGWTEvent(), event.getSource());
                }
                else {
                    eventBus.fireEvent(event.asGWTEvent());
                }
            }
            long t1 = TimeUtil.getCurrentTime();
            GWT.log("[Dispatch] Dispatched events in " + (t1 - t0) + " ms");
        }
    }

    private void handleError(final Throwable throwable, final Action<?> action, final DispatchServiceCallback<?> callback) {
        if (throwable instanceof PermissionDeniedException) {
            // Try to determine if the user is logged in.  The session might have expired.
            UserInSession userInSession = ((PermissionDeniedException) throwable).getUserInSession();
            if(userInSession.isGuest()) {
                // Set up next place
                Place continueTo = placeController.getWhere();
                loggedInUser.setLoggedInUser(userInSession);
                GWT.log("[Dispatch] Permission denied.  User is the guest user so redirecting to login.");
                signInRequiredHandler.handleSignInRequired(continueTo);
            }
        }
        // Skip handling for actions that do not care about errors
        if(action instanceof InvocationExceptionTolerantAction) {
            Optional<String> errorMessage = ((InvocationExceptionTolerantAction) action).handleInvocationException((InvocationException) throwable);
            errorMessage.ifPresent(this::displayAlert);
            return;
        }
        callback.onFailure(throwable);
    }

    private void displayAlert(String alert) {
        messageBox.showAlert(alert);
    }
}
