package edu.stanford.bmir.protege.web.client.inject;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public class PlaceControllerProvider implements Provider<PlaceController> {

    private EventBus eventBus;

    @Inject
    public PlaceControllerProvider(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public PlaceController get() {
        return new PlaceController(eventBus);
    }
}
