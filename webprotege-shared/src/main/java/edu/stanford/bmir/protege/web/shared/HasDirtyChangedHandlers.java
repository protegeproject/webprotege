package edu.stanford.bmir.protege.web.shared;


import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public interface HasDirtyChangedHandlers extends HasHandlers {

    HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler);
}
