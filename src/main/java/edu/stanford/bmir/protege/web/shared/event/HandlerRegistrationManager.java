package edu.stanford.bmir.protege.web.shared.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/04/2013
 */
public class HandlerRegistrationManager {

    private List<HandlerRegistration> handlerRegistrationList = new ArrayList<HandlerRegistration>();

    private final EventBus eventBus;

    public HandlerRegistrationManager(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void addHandlerRegistration(HandlerRegistration handlerRegistration) {
        handlerRegistrationList.add(handlerRegistration);
    }

    public void removeHandlers() {
        for(HandlerRegistration handlerRegistration : handlerRegistrationList) {
            handlerRegistration.removeHandler();
        }
        handlerRegistrationList.clear();
    }

    /**
     */
    public <H> void registerHandler(Event.Type<H> type, H handler) {
        HandlerRegistration registration = eventBus.addHandler(type, handler);
        addHandlerRegistration(registration);
    }

    public <H> void registerHandlerToProject(ProjectId projectId, Event.Type<H> type, H handler) {
        HandlerRegistration registration = eventBus.addHandlerToSource(type, projectId, handler);
        addHandlerRegistration(registration);
    }
}
