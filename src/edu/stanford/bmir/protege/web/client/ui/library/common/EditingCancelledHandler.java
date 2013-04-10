package edu.stanford.bmir.protege.web.client.ui.library.common;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/03/2013
 */
public interface EditingCancelledHandler extends EventHandler {

    void editingCancelled(EditingCancelledEvent event);
}
