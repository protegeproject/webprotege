package edu.stanford.bmir.protege.web.client.dispatch.cache;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/11/2013
 */
public interface ResultCachingStrategy<A extends Action<R>, R extends Result, K> {

    Class<A> getActionClass();


    boolean shouldCache(A action, R result);

    Collection<K> getInvalidationKeys(A action, R result);

    void setInvalidationEventHandler(ResultCacheInvalidationEventHandler handler);

    void registerEventHandlers();

}
