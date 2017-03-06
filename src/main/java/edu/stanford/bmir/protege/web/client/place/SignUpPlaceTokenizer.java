package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/16
 */
public class SignUpPlaceTokenizer implements PlaceTokenizer<SignUpPlace> {

    @Override
    public SignUpPlace getPlace(String token) {
        return new SignUpPlace();
    }

    @Override
    public String getToken(SignUpPlace place) {
        Optional<Place> continueTo = place.getContinueTo();
        if(continueTo.isPresent()) {
            return "/signup";
        }
        else {
            return "/signup";
        }
    }
}
