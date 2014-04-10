package edu.stanford.bmir.protege.web.shared.event;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/12/2012
 */
public class EventBusManager {

    private static final EventBusManager instance = new EventBusManager();


    private final EventBus globalEventBus = new SimpleEventBus();

    static {
        getManager();
    }

    private EventBusManager() {
    }

    public static EventBusManager getManager() {
        return instance;
    }

    /**
     * Posts an event onto the event bus.
     * @param event The event to be posted.  Not {@code null}.
     */
    public void postEvent(Event<?> event) {
        checkNotNull(event, "Event must not be null");
        try {
            Object source = event.getSource();
            if (source != null) {
                globalEventBus.fireEventFromSource(event, source);
            }
            globalEventBus.fireEvent(event);
        }
        catch (Exception e) {
            GWT.log("Exception caught whilst dispatching event", e);
        }
    }

    public void postEvents(List<? extends Event<?>> events) {
        checkNotNull(events, "Events must not be null");
        for(Event<?> event : events) {
            postEvent(event);
        }
    }

    /**
     */
    public <H> HandlerRegistration registerHandler(Event.Type<H> type, H handler) {
        return globalEventBus.addHandler(type, handler);
    }

    public <H> HandlerRegistration registerHandlerToProject(ProjectId projectId, Event.Type<H> type, H handler) {
        return globalEventBus.addHandlerToSource(type, projectId, handler);
    }

//    public <H> HandlerRegistration registerHandler(ProjectId projectId, Event.Type<H> type, H handler) {
//        Optional<Project> project = ProjectManager.get().getProject(projectId);
//        if(project.isPresent()) {
//            return project.get().getEventBus().addHandler(type, handler);
//        }
//        else {
//            throw new RuntimeException("Cannot add handler.  Unknown project: " + projectId);
//        }
//    }




}
