package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.inject.Inject;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/04/2013
 */
@ApplicationSingleton
public class PlaceManager {

    private PlaceController placeController;

    @Inject
    public PlaceManager(EventBus eventBus) {
        placeController = new PlaceController(eventBus);

        WebProtegePlaceHistoryMapper placeHistoryMapper = GWT.create(WebProtegePlaceHistoryMapper.class);
        PlaceHistoryHandler placeHistoryHandler = new PlaceHistoryHandler(placeHistoryMapper);
        placeHistoryHandler.register(placeController, eventBus, Place.NOWHERE);
        placeHistoryHandler.handleCurrentHistory();
    }


    public Place getCurrentPlace() {
        return placeController.getWhere();
    }

    public void setCurrentPlace(Place place) {
        if (!place.equals(placeController.getWhere())) {
            GWT.log("[PlaceManager] Setting current place: " + place);
        }
        placeController.goTo(place);
    }

}
