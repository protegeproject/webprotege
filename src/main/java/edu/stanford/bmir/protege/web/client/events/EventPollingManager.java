package edu.stanford.bmir.protege.web.client.events;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class EventPollingManager {

    private final DispatchServiceManager dispatchServiceManager;

    private int pollingPeriodInMS;

    private Timer pollingTimer;

    private EventTag nextTag = EventTag.getFirst();

    private ProjectId projectId;

    private EventBus eventBus;

    public static EventPollingManager get(int pollingPeriodInMS, ProjectId projectId, EventBus eventBus, DispatchServiceManager dispatchServiceManager) {
        return new EventPollingManager(pollingPeriodInMS, projectId, eventBus, dispatchServiceManager);
    }

    private EventPollingManager(int pollingPeriodInMS, ProjectId projectId, EventBus eventBus, DispatchServiceManager dispatchServiceManager) {
        this.eventBus = eventBus;
        if(pollingPeriodInMS < 1) {
            throw new IllegalArgumentException("pollingPeriodInMS must be greater than 0");
        }
        this.pollingPeriodInMS = pollingPeriodInMS;
        this.projectId = checkNotNull(projectId, "projectId must not be null");
//        this.dispatchManager = checkNotNull(dispatchManager, "dispatchManager must not be null");
        pollingTimer = new Timer() {
            @Override
            public void run() {
                pollForProjectEvents();
            }
        };
        this.dispatchServiceManager = dispatchServiceManager;

    }

    public void start() {
        pollingTimer.scheduleRepeating(pollingPeriodInMS);
    }

    public void stop() {
        pollingTimer.cancel();
    }


    public void pollForProjectEvents() {
        GWT.log("[Event Polling Manager] Polling for project events for " + projectId + " from " + nextTag);
        UserId userId = Application.get().getUserId();
        dispatchServiceManager.execute(new GetProjectEventsAction(nextTag, projectId, userId), new DispatchServiceCallback<GetProjectEventsResult>() {

            @Override
            public void handleSuccess(GetProjectEventsResult result) {
                dispatchEvents(result.getEvents());
            }
        });
    }


    public void dispatchEvents(EventList<?> eventList) {
        if(eventList.isEmpty()) {
            return;
        }
        GWT.log("[Event Polling Manager] Retrieved " + eventList.getEvents().size() + " events from server. From " + eventList.getStartTag() + " to " + eventList.getEndTag());
        EventTag eventListStartTag = eventList.getStartTag();
        if(!eventList.getStartTag().equals(eventList.getEndTag()) && nextTag.isGreaterOrEqualTo(eventListStartTag)) {
            // We haven't missed any events - our next retrieval will be from where we got the event to.
            nextTag = eventList.getEndTag();
            GWT.log("[Event Polling Manager] Updated events.  Next tag is " + nextTag);
        }
        if (!eventList.isEmpty()) {
            GWT.log("[Event Polling Manager] Dispatching events from polling manager...");
            for(Event<?> event : eventList.getEvents()) {
                GWT.log("[Event Polling Manager] Event: " + event.toString());
                if (event.getSource() != null) {
                    eventBus.fireEventFromSource(event, event.getSource());
                }
                else {
                    eventBus.fireEvent(event);
                }
            }
        }
    }

}
