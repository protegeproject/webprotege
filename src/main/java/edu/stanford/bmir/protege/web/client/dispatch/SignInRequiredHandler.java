package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.place.shared.Place;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public interface SignInRequiredHandler {

    void handleSignInRequired(@Nonnull Place continueTo);
}
