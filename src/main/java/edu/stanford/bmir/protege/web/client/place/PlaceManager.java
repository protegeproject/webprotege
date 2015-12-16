package edu.stanford.bmir.protege.web.client.place;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/04/2013
 */
public class PlaceManager {

    private static final ProjectListPlace DEFAULT_PLACE = ProjectListPlace.DEFAULT_PLACE;

    private PlaceController placeController;

    public PlaceManager() {
        EventBus placeEventBus = new SimpleEventBus();
        placeEventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
            @Override
            public void onPlaceChange(PlaceChangeEvent event) {
                // Fire it on to the real event bus
                EventBusManager.getManager().postEvent(event);
            }
        });
        placeController = new PlaceController(placeEventBus);

        WebProtegePlaceHistoryMapper placeHistoryMapper = GWT.create(WebProtegePlaceHistoryMapper.class);
        PlaceHistoryHandler placeHistoryHandler = new PlaceHistoryHandler(placeHistoryMapper);
        placeHistoryHandler.register(placeController, placeEventBus, Place.NOWHERE);
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
