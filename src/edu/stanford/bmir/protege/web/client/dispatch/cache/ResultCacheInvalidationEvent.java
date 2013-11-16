package edu.stanford.bmir.protege.web.client.dispatch.cache;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/11/2013
 */
public class ResultCacheInvalidationEvent extends Event<ResultCacheInvalidationEventHandler> {

    public static final Type<ResultCacheInvalidationEventHandler> TYPE = new Type<ResultCacheInvalidationEventHandler>();

    private Class<? extends Action<?>> actionClass;

    private Collection<?> invalidationKeys;

    public ResultCacheInvalidationEvent(Class<? extends Action<?>> actionClass, Collection<?> invalidationKeys) {
        this.actionClass = checkNotNull(actionClass);
        this.invalidationKeys = new ArrayList<Object>(checkNotNull(invalidationKeys));
    }

    @Override
    public Type<ResultCacheInvalidationEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ResultCacheInvalidationEventHandler handler) {
        handler.handleInvalidationEvent(this);
    }

    public Class<? extends Action<?>> getActionClass() {
        return actionClass;
    }

    public Collection<?> getInvalidationKeys() {
        return new ArrayList<Object>(invalidationKeys);
    }
}
