package edu.stanford.bmir.protege.web.client.login;

import com.google.gwt.place.shared.Place;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class LoginPlace extends Place {


    private Optional<Place> continueTo;

    public LoginPlace() {
        continueTo = Optional.empty();
    }

    public LoginPlace(Place continueTo) {
        this.continueTo = Optional.of(continueTo);
    }

    public Optional<Place> getContinueTo() {
        return continueTo;
    }


}
