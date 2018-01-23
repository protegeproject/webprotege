package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.login.LoginPlace;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class PlaceHistoryHandlerProvider implements Provider<PlaceHistoryHandler> {

    private final WebProtegePlaceHistoryMapper placeHistoryMapper;

    private final PlaceController placeController;

    private final EventBus eventBus;

    private final Place defaultPlace;

    @Inject
    public PlaceHistoryHandlerProvider(WebProtegePlaceHistoryMapper placeHistoryMapper,
                                       PlaceController placeController,
                                       EventBus eventBus) {
        this.placeHistoryMapper = placeHistoryMapper;
        this.placeController = placeController;
        this.eventBus = eventBus;
        this.defaultPlace = new LoginPlace();
    }

    @Override
    public PlaceHistoryHandler get() {
        PlaceHistoryHandler handler = new PlaceHistoryHandler(placeHistoryMapper, new PlaceHistoryHandler.DefaultHistorian() {
            @Override
            public String getToken() {
                GWT.log("[Historian] Token: " + super.getToken());
                return super.getToken();
            }
        });
        handler.register(placeController, eventBus, defaultPlace);
        return handler;
    }
}
