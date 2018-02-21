package edu.stanford.bmir.protege.web.shared;


import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public interface DirtyChangedHandler extends EventHandler {

    void handleDirtyChanged(DirtyChangedEvent event);
}
