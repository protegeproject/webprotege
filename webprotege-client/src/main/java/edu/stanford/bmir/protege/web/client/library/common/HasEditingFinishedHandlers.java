package edu.stanford.bmir.protege.web.client.library.common;


import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2013
 */
public interface HasEditingFinishedHandlers<T> extends HasHandlers {

    HandlerRegistration addEditingFinishedHandler(EditingFinishedHandler<T> handler);
}
