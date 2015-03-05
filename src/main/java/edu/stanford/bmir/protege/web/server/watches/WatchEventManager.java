package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.event.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class WatchEventManager {

    private EventManager<ProjectEvent<?>> eventManager;

    private WatchManager watchManager;

    public WatchEventManager(WatchManager watchManager, EventManager<ProjectEvent<?>> eventManager) {
        this.watchManager = checkNotNull(watchManager);
        this.eventManager = checkNotNull(eventManager);
    }

    public void attach() {

        eventManager.addHandler(ClassFrameChangedEvent.TYPE, new ClassFrameChangedEventHandler() {
            @Override
            public void classFrameChanged(ClassFrameChangedEvent event) {
                watchManager.handleEntityFrameChanged(event.getEntity());
            }
        });
        eventManager.addHandler(ObjectPropertyFrameChangedEvent.TYPE, new ObjectPropertyFrameChangedEventHandler() {
            @Override
            public void objectPropertyFrameChanged(ObjectPropertyFrameChangedEvent event) {
                watchManager.handleEntityFrameChanged(event.getEntity());
            }
        });
        eventManager.addHandler(DataPropertyFrameChangedEvent.TYPE, new DataPropertyFrameChangedEventHandler() {
            @Override
            public void dataPropertyFrameChanged(DataPropertyFrameChangedEvent event) {
                watchManager.handleEntityFrameChanged(event.getEntity());
            }
        });
        eventManager.addHandler(AnnotationPropertyFrameChangedEvent.TYPE, new AnnotationPropertyFrameChangedEventHandler() {
            @Override
            public void annotationPropertyFrameChanged(AnnotationPropertyFrameChangedEvent event) {
                watchManager.handleEntityFrameChanged(event.getEntity());
            }
        });
        eventManager.addHandler(NamedIndividualFrameChangedEvent.TYPE, new NamedIndividualFrameChangedEventHandler() {
            @Override
            public void namedIndividualFrameChanged(NamedIndividualFrameChangedEvent event) {
                watchManager.handleEntityFrameChanged(event.getEntity());
            }
        });
    }
}
