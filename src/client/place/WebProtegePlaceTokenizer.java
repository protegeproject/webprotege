package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
public interface WebProtegePlaceTokenizer<P extends Place> extends PlaceTokenizer<P>  {

    boolean matches(String token);

    Class<P> getPlaceClass();
}
