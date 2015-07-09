package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.event.*;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class WatchEventManagerImpl implements WatchEventManager {

    private EventManager<ProjectEvent<?>> eventManager;

    private WatchManager watchManager;

    @Inject
    public WatchEventManagerImpl(WatchManager watchManager, EventManager<ProjectEvent<?>> eventManager) {
        this.watchManager = checkNotNull(watchManager);
        this.eventManager = checkNotNull(eventManager);
    }

    public void attach() {
        // Note, there is no need to keep hold of Handler Registrations here as these will be cleaned up and
        // terminated when the relevant project is disposed.
        eventManager.addHandler(ClassFrameChangedEvent.TYPE, event -> {
            watchManager.handleEntityFrameChanged(event.getEntity());
        });
        eventManager.addHandler(ObjectPropertyFrameChangedEvent.TYPE, event -> {
            watchManager.handleEntityFrameChanged(event.getEntity());
        });
        eventManager.addHandler(DataPropertyFrameChangedEvent.TYPE, event -> {
            watchManager.handleEntityFrameChanged(event.getEntity());
        });
        eventManager.addHandler(AnnotationPropertyFrameChangedEvent.TYPE, event -> {
            watchManager.handleEntityFrameChanged(event.getEntity());
        });
        eventManager.addHandler(NamedIndividualFrameChangedEvent.TYPE, event -> {
            watchManager.handleEntityFrameChanged(event.getEntity());
        });
    }
}
