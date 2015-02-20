package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.UmbrellaException;
import edu.stanford.bmir.protege.web.client.dispatch.cache.ResultCache;
import edu.stanford.bmir.protege.web.client.rpc.RenderingServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasBrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.dispatch.InvocationExceptionTolerantAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.SerializableEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public class DispatchServiceManager {

    private static DispatchServiceManager instance;


    private DispatchServiceAsync async;


    private DispatchServiceManager() {
        async = GWT.create(DispatchService.class);
    }


    public static DispatchServiceManager get() {
        if(instance == null) {
            instance = new DispatchServiceManager();
        }
        return instance;
    }

    private int requestCount;

    private Map<ProjectId, ResultCache> resultCacheMap = new HashMap<ProjectId, ResultCache>();

    private ResultCache getResultCache(ProjectId projectId) {
        ResultCache resultCache = resultCacheMap.get(projectId);
        if(resultCache == null) {
            resultCache = new ResultCache(projectId);
            resultCacheMap.put(projectId, resultCache);
        }
        return resultCache;
    }

    @SuppressWarnings("unchecked")
    public <A extends Action<R>, R extends Result> void execute(A action, final AsyncCallback<R> callback) {
        if(action instanceof HasProjectId) {
            ProjectId projectId = ((HasProjectId) action).getProjectId();
            ResultCache resultCache = getResultCache(projectId);
            Optional<R> result = resultCache.getCachedResult(action);
            if(result.isPresent()) {
                GWT.log("[DISPATCH] Using cached result (" + action + ")");
                callback.onSuccess(result.get());
                return;
            }
        }

        GWT.log("[DISPATCH] Making request to server.  Request " + requestCount + ". (" + action + ")");
        requestCount++;
        async.executeAction(action, new AsyncCallbackProxy(action, callback));
    }


    private class AsyncCallbackProxy<R extends Result> implements AsyncCallback<DispatchServiceResultContainer> {

        private Action<?> action;

        private AsyncCallback<Result> delegate;

        public AsyncCallbackProxy(Action<?> action, AsyncCallback<Result> delegate) {
            this.delegate = delegate;
            this.action = action;
        }

        @Override
        public void onFailure(Throwable caught) {
            Optional<Throwable> passOn = handleError(caught, action);
            if (passOn.isPresent()) {
                delegate.onFailure(passOn.get());
            }
        }

        @Override
        public void onSuccess(DispatchServiceResultContainer result) {
            // TODO: Fix
            if(action instanceof HasProjectId) {
                ResultCache resultCache = getResultCache(((HasProjectId) action).getProjectId());
                resultCache.cacheResult((Action<R>) action, (R) result.getResult());
            }
            cacheRenderables(result.getResult());
            dispatchEvents(result.getResult());
            delegate.onSuccess(result.getResult());

        }

    }


    private void cacheRenderables(Object result) {
        if (result instanceof HasBrowserTextMap) {
            BrowserTextMap browserTextMap = ((HasBrowserTextMap) result).getBrowserTextMap();
            RenderingServiceManager.getManager().registerEntityData(browserTextMap.getOWLEntityData());
        }
    }

    private void dispatchEvents(Object result) {
        if(result instanceof HasEventList<?>) {
            EventList<? extends SerializableEvent<?>> eventList = ((HasEventList<? extends SerializableEvent<?>>) result).getEventList();

            List<? extends SerializableEvent<?>> events = eventList.getEvents();
            // TODO: FIX - Should be dispatched by the project event manager otherwise we will get events from the
            // TODO: more than once!

            for(Event<?> event : events) {
                GWT.log("[DISPATCH] Dispatching event (" + event.toDebugString() + ")");
            }
            EventBusManager.getManager().postEvents(events);
        }
    }

    private Optional<Throwable> handleError(Throwable throwable, Action<?> action) {
        displayAlert(throwable.toString());
        // Only ActionExecutionException and PermissionDeniedException are declared in the service method.
        // We therefore handle these at the top level.
        if(throwable instanceof ActionExecutionException) {
            if(action instanceof InvocationExceptionTolerantAction) {
                Optional<String> errorMessage = ((InvocationExceptionTolerantAction) action).handleInvocationException((InvocationException) throwable);
                if(errorMessage.isPresent()) {
                    displayAlert(errorMessage.get());
                }
            }
            return Optional.of(throwable);
        }
        else if(throwable instanceof PermissionDeniedException) {
            displayAlert("You do not have permission to carry out the specified action");
            return Optional.of(throwable);
        }
        else if(throwable instanceof IncompatibleRemoteServiceException) {
            displayAlert("WebProtege has been upgraded.  Please refresh your browser.");
            GWT.log("Incompatible remote service exception", throwable);
            return Optional.absent();
        }
        else if(throwable instanceof InvocationException) {
            if(action instanceof InvocationExceptionTolerantAction) {
                Optional<String> errorMessage = ((InvocationExceptionTolerantAction) action).handleInvocationException((InvocationException) throwable);
                if(errorMessage.isPresent()) {
                    displayAlert(errorMessage.get());
                }
            }
            else {
                displayAlert("There was a problem communicating with the server and the operation could not be completed.  Please check your network connection and try again.");
            }
            return Optional.absent();
        }
        else if(throwable instanceof UmbrellaException) {
            displayAlert("An unexpected problem occurred and your actions could not be completed.  Please try again.");
            return Optional.absent();
        }
        else {
            return Optional.of(throwable);
        }

    }

    private void displayAlert(String alert) {
        MessageBox.showAlert(alert);
    }
}
