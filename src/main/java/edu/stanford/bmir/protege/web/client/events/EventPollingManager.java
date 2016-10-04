package edu.stanford.bmir.protege.web.client.events;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;

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

    private final ProjectId projectId;

    private final EventBus eventBus;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public EventPollingManager(@EventPollingPeriod int pollingPeriodInMS,
                               ProjectId projectId,
                               EventBus eventBus,
                               DispatchServiceManager dispatchServiceManager,
                               LoggedInUserProvider loggedInUserProvider) {
        this.eventBus = eventBus;
        this.loggedInUserProvider = loggedInUserProvider;
        if(pollingPeriodInMS < 1) {
            throw new IllegalArgumentException("pollingPeriodInMS must be greater than 0");
        }
        this.pollingPeriodInMS = pollingPeriodInMS;
        this.projectId = checkNotNull(projectId, "projectId must not be null");
        pollingTimer = new Timer() {
            @Override
            public void run() {
                pollForProjectEvents();
            }
        };
        this.dispatchServiceManager = dispatchServiceManager;

    }

    public void start() {
        if(pollingTimer.isRunning()) {
            return;
        }
        pollingTimer.scheduleRepeating(pollingPeriodInMS);
    }

    public void stop() {
        pollingTimer.cancel();
    }


    public void pollForProjectEvents() {
        GWT.log("[Event Polling Manager] Polling for project events for " + projectId + " from " + nextTag);
        UserId userId = loggedInUserProvider.getCurrentUserId();
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
            for(WebProtegeEvent<?> event : eventList.getEvents()) {
                GWT.log("[Event Polling Manager] Event: " + event.toString());
                if (event.getSource() != null) {
                    eventBus.fireEventFromSource(event.asGWTEvent(), event.getSource());
                }
                else {
                    eventBus.fireEvent(event.asGWTEvent());
                }
            }
        }
    }

}
