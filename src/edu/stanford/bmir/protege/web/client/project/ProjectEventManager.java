package edu.stanford.bmir.protege.web.client.project;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import edu.stanford.bmir.protege.web.client.events.EventDispatchManager;
import edu.stanford.bmir.protege.web.client.events.EventPollingManager;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/03/2013
 */
public class ProjectEventManager implements EventDispatchManager {

    public static final int POLLING_PERIOD_IN_MS = 10 * 1000;

    private final EventPollingManager eventPollingManager;

    private final EventBus projectEventBus;

    private final ProjectId projectId;


    public ProjectEventManager(ProjectId projectId) {
        this.projectId = projectId;
        projectEventBus = new SimpleEventBus();
        eventPollingManager = EventPollingManager.get(POLLING_PERIOD_IN_MS, projectId);
    }


    public <H> HandlerRegistration addHandler(Event.Type<H> type, H handler) {
        return projectEventBus.addHandler(type, handler);
    }


    public void dispatchEvents(EventList<?> eventList) {
        eventPollingManager.dispatchEvents(eventList);
    }

    @Override
    public void dispatchEvents(List<Event<?>> events) {
        for(Event<?> event : events) {
            if (projectId.equals(event.getSource())) {
                projectEventBus.fireEvent(event);
            }
        }
    }
}
