package edu.stanford.bmir.protege.web.client.login;

import com.google.common.base.Optional;
import com.google.gwt.place.shared.Place;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class LoginPlace extends Place {


    private Optional<Place> continueTo;

    public LoginPlace() {
        continueTo = Optional.absent();
    }

    public LoginPlace(Place continueTo) {
        this.continueTo = Optional.<Place>of(continueTo);
    }

    public Optional<Place> getContinueTo() {
        return continueTo;
    }


}
