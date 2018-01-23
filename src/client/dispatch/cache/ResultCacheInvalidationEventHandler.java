package edu.stanford.bmir.protege.web.client.dispatch.cache;

import com.google.gwt.event.shared.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/11/2013
 */
public interface ResultCacheInvalidationEventHandler extends EventHandler {

    void handleInvalidationEvent(ResultCacheInvalidationEvent event);
}
