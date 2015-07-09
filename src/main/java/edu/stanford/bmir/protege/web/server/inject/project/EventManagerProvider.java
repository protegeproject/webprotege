package edu.stanford.bmir.protege.web.server.inject.project;

import edu.stanford.bmir.protege.web.server.events.EventLifeTime;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.inject.Provider;
import java.util.concurrent.TimeUnit;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 08/07/15
 */
public class EventManagerProvider implements Provider<EventManager<ProjectEvent<?>>> {

    public static final EventLifeTime PROJECT_EVENT_LIFE_TIME = EventLifeTime.get(60, TimeUnit.SECONDS);

    public EventManagerProvider() {
    }

    @Override
    public EventManager<ProjectEvent<?>> get() {
        return new EventManager<>(PROJECT_EVENT_LIFE_TIME);
    }
}
