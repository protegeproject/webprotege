package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.UmbrellaException;
import edu.stanford.bmir.protege.web.client.rpc.RenderingServiceManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasBrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.SerializableEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public class DispatchServiceManager {

    private static final DispatchServiceManager instance = new DispatchServiceManager();


    private DispatchServiceAsync async = GWT.create(DispatchService.class);

    private DispatchServiceManager() {

    }


    public static DispatchServiceManager get() {
        return instance;
    }

    private int requestCount;

    @SuppressWarnings("unchecked")
    public <A extends Action<R>, R extends Result> void execute(A action, final AsyncCallback<R> callback) {
        GWT.log("Making request to server: " + requestCount);
        requestCount++;
        async.executeAction(action, new AsyncCallbackProxy(action, callback));
    }


    private class AsyncCallbackProxy<R extends Result> implements AsyncCallback<DispatchServiceResultContainer<R>> {

        private Action<?> action;

        private AsyncCallback<R> delegate;

        public AsyncCallbackProxy(Action<?> action, AsyncCallback<R> delegate) {
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
        public void onSuccess(DispatchServiceResultContainer<R> result) {
            // TODO: Fix
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
            GWT.log("Result has returned events.  Dispatching events");
            EventList<? extends SerializableEvent<?>> eventList = ((HasEventList<? extends SerializableEvent<?>>) result).getEventList();

            List<? extends SerializableEvent<?>> events = eventList.getEvents();
            // TODO: FIX - Should be dispatched by the project event manager otherwise we will get events from the
            // TODO: more than once!

            for(Event<?> event : events) {
                GWT.log(event.toString());
            }
            EventBusManager.getManager().postEvents(events);
        }
    }

    private Optional<Throwable> handleError(Throwable throwable, Action<?> action) {
        if(throwable instanceof IncompatibleRemoteServiceException) {
            displayAlert("Maintenance has been performed on the server.  Please refresh your browser.");
            GWT.log("Incompatible remote service exception", throwable);
            return Optional.absent();
        }
        else if(throwable instanceof InvocationException) {
            displayAlert("There was a problem communicating with the server and the operation could not be completed.  Please check your network connection and try again.");
            return Optional.absent();
        }
//        else if(throwable instanceof UnexpectedException) {
//            displayAlert("An unexpected error occurred: " + throwable.getMessage());
//            return Optional.absent();
//        }
        else if(throwable instanceof UmbrellaException) {
            for(Throwable cause : ((UmbrellaException) throwable).getCauses()) {
                if(cause instanceof PermissionDeniedException) {
                    displayAlert("You do not have permission to perform the requested operation.");
                    return Optional.absent();
                }
            }
            displayAlert("An unexpected problem occurred and your actions could not be completed.  Please try again.");
            return Optional.absent();
        }
        else if(throwable instanceof PermissionDeniedException) {
            displayAlert("You do not have permission to carry out the specified action");
            return Optional.of(throwable);
        }
        else {
            return Optional.of(throwable);
        }

    }

    private void displayAlert(String alert) {
        Window.alert(alert);
    }
}
