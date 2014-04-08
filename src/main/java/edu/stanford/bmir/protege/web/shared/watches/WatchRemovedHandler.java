package edu.stanford.bmir.protege.web.shared.watches;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public interface WatchRemovedHandler extends EventHandler {

    void handleWatchRemoved(WatchRemovedEvent event);
}
