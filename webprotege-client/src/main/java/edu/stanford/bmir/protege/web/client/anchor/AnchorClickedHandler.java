package edu.stanford.bmir.protege.web.client.anchor;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/01/2014
 */
public interface AnchorClickedHandler extends EventHandler {

    void handleAnchorClicked(AnchorClickedEvent event);
}
