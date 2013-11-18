package edu.stanford.bmir.protege.web.client.dispatch.cache;

import edu.stanford.bmir.protege.web.client.dispatch.cache.ResultCacheInvalidationEvent;
import edu.stanford.bmir.protege.web.client.dispatch.cache.ResultCacheInvalidationEventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/11/2013
 */
public class NullResultCacheInvalidationEventHandler implements ResultCacheInvalidationEventHandler {

    @Override
    public void handleInvalidationEvent(ResultCacheInvalidationEvent event) {
    }
}
