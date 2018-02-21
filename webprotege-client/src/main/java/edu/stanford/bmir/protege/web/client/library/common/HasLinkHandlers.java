package edu.stanford.bmir.protege.web.client.library.common;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/12/2012
 */
public interface HasLinkHandlers {

    HandlerRegistration addLinkHandler(LinkClickedHandler handler);

}
