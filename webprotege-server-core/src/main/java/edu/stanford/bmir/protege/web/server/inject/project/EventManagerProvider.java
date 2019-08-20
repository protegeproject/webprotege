package edu.stanford.bmir.protege.web.server.inject.project;

import edu.stanford.bmir.protege.web.server.events.EventLifeTime;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.project.ProjectDisposablesManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 08/07/15
 */
public class EventManagerProvider implements Provider<EventManager<ProjectEvent<?>>> {

    public static final EventLifeTime PROJECT_EVENT_LIFE_TIME = EventLifeTime.get(60, TimeUnit.SECONDS);

    private final ProjectDisposablesManager projectDisposablesManager;

    @Inject
    public EventManagerProvider(ProjectDisposablesManager projectDisposablesManager) {
        this.projectDisposablesManager = checkNotNull(projectDisposablesManager);
    }

    @Override
    public EventManager<ProjectEvent<?>> get() {
        EventManager<ProjectEvent<?>> projectEventEventManager = new EventManager<>(PROJECT_EVENT_LIFE_TIME);
        projectDisposablesManager.register(projectEventEventManager);
        return projectEventEventManager;
    }
}
