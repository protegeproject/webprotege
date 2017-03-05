package edu.stanford.bmir.protege.web.shared.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.ResettableEventBus;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Mar 2017
 */
public class WebProtegeEventBus implements HasDispose {

    private final ResettableEventBus eventBus;

    @Inject
    public WebProtegeEventBus(EventBus eventBus) {
        this.eventBus = new ResettableEventBus(eventBus);
    }

    public <T> void addProjectEventHandler(ProjectId projectId, Event.Type<T> type, T handler) {
        eventBus.addHandlerToSource(type, projectId, handler);
    }

    public <T> void addApplicationEventHandler(Event.Type<T> type, T handler) {
        eventBus.addHandler(type, handler);
    }

    @Override
    public void dispose() {
        eventBus.removeHandlers();
    }
}
