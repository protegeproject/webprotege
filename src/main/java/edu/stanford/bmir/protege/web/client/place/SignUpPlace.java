package edu.stanford.bmir.protege.web.client.place;

import com.google.common.base.Optional;
import com.google.gwt.place.shared.Place;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public class SignUpPlace extends Place {

    private Optional<Place> continueTo;

    public SignUpPlace() {
        continueTo = Optional.absent();
    }

    public SignUpPlace(Place continueTo) {
        this.continueTo = Optional.<Place>of(continueTo);
    }

    public Optional<Place> getContinueTo() {
        return continueTo;
    }
}
