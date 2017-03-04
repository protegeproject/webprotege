package edu.stanford.bmir.protege.web.client.anchor;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/01/2014
 */
public interface HasAnchorClickedHandlers {

    HandlerRegistration addAnchorClickedHandler(AnchorClickedHandler handler);
}
