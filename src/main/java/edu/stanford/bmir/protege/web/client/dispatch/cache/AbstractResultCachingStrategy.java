package edu.stanford.bmir.protege.web.client.dispatch.cache;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/11/2013
 */
public abstract class AbstractResultCachingStrategy<A extends Action<R>, R extends Result, K> implements ResultCachingStrategy<A, R, K>, HasDispose {

    private ResultCacheInvalidationEventHandler handler = new NullResultCacheInvalidationEventHandler();

    private List<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

    private ProjectId projectId;

    private final EventBus eventBus;

    protected AbstractResultCachingStrategy(ProjectId projectId, EventBus eventBus) {
        this.projectId = checkNotNull(projectId);
        this.eventBus = checkNotNull(eventBus);
    }

    @Override
    public void setInvalidationEventHandler(ResultCacheInvalidationEventHandler handler) {
        this.handler = checkNotNull(handler);
    }

    protected void fireResultsInvalidatedEvent(Collection<K> invalidationKeys) {
        handler.handleInvalidationEvent(new ResultCacheInvalidationEvent(getActionClass(), invalidationKeys));
    }

    protected void fireResultsInvalidatedEvent(K invalidationKey) {
        Set<K> invalidationKeys = Collections.singleton(invalidationKey);
        handler.handleInvalidationEvent(new ResultCacheInvalidationEvent(getActionClass(), invalidationKeys));
    }

    protected  <T> void registerProjectEventHandler(Event.Type<T> type, T handler) {
        HandlerRegistration reg = eventBus.addHandlerToSource(checkNotNull(type), projectId, checkNotNull(handler));
        handlerRegistrations.add(reg);
    }

    @Override
    public final void dispose() {
        for(HandlerRegistration handlerRegistration : handlerRegistrations) {
            handlerRegistration.removeHandler();
        }
    }


}
