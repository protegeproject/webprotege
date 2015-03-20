package edu.stanford.bmir.protege.web.client.dispatch.cache;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/11/2013
 */
public class ResultCache {


    private boolean enabled = false;

    private RemovalListener<Action<?>, Result> removalListener = new RemovalListener<Action<?>, Result>() {
        @Override
        public void onRemoval(RemovalNotification<Action<?>, Result> notification) {
            GWT.log("Item removed from cache: " + notification);
            Action<?> key = notification.getKey();
            if (key != null) {
            }
        }
    };

    private Map<Class<? extends Action<?>>, ResultCachingStrategy<?,?,?>> actionClass2InvalidatorMap = new HashMap<Class<? extends Action<?>>, ResultCachingStrategy<?, ?, ?>>();

    private Cache<Action<?>, Result> resultCache = CacheBuilder.newBuilder().maximumSize(1000).removalListener(removalListener).build();

    private Multimap<InvalidationKeyWrapper, Action> invalidationKey2ActionMap = HashMultimap.create();

    public ResultCache(ProjectId projectId) {
        install(new GetClassFrameActionResultCachingStrategy(projectId));
        install(new LookupEntitiesActionResultCachingStrategy(projectId));
    }

    private void install(ResultCachingStrategy<?,?,?> invalidator) {
        actionClass2InvalidatorMap.put(invalidator.getActionClass(), invalidator);
        invalidator.setInvalidationEventHandler(new ResultCacheInvalidationEventHandler() {
            @Override
            public void handleInvalidationEvent(ResultCacheInvalidationEvent event) {
                removeInvalidResults(event);
            }
        });
        invalidator.registerEventHandlers();
    }

    @SuppressWarnings("unchecked")
    public <A extends Action<R>, R extends Result> Optional<R> getCachedResult(A action) {
        if(!enabled) {
            return Optional.absent();
        }
        Result result = resultCache.getIfPresent(action);
        if(result != null) {
            GWT.log("[Result Cache] Found cached result for " + action + "  --> " + result);
        }
        return Optional.fromNullable((R) result);
    }

    @SuppressWarnings("unchecked")
    public <A extends Action<R>, R extends Result> void cacheResult(A action, R result) {
        if(!enabled) {
            return;
        }
        Class<? extends Action<?>> actionClass = (Class<? extends Action<?>>) action.getClass();
        ResultCachingStrategy<A,R,?> invalidator = (ResultCachingStrategy<A,R,?>) actionClass2InvalidatorMap.get(actionClass);
        if(invalidator == null) {
            return;
        }
        if(!invalidator.shouldCache(action, result)) {
            return;
        }
        GWT.log("[Result Cache] Caching result for action. Action=" + action + "   Result=" + result);
        Collection<?> invalidationKeys = invalidator.getInvalidationKeys(action, result);
        for(Object key : invalidationKeys) {
            InvalidationKeyWrapper keyWrapper = new InvalidationKeyWrapper(actionClass, key);
            invalidationKey2ActionMap.put(keyWrapper, action);
        }
        resultCache.put(action, result);
    }

    private void removeInvalidResults(ResultCacheInvalidationEvent event) {
        for(Object key : event.getInvalidationKeys()) {
            InvalidationKeyWrapper wrapper = new InvalidationKeyWrapper(event.getActionClass(), key);
            for(Action<?> action : invalidationKey2ActionMap.get(wrapper)) {
                GWT.log("[Result Cache] Removing invalid result from cache: " + action);
                resultCache.invalidate(action);
            }
        }
    }


    private static class InvalidationKeyWrapper {

        private Class<? extends Action<?>> actionClass;

        private Object key;

        private InvalidationKeyWrapper(Class<? extends Action<?>> actionClass, Object key) {
            this.key = key;
            this.actionClass = actionClass;
        }

        @Override
        public int hashCode() {
            return "ResultCache$InvalidationKeyWrapper".hashCode() + actionClass.hashCode() + key.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if(o == this) {
                return true;
            }
            if(!(o instanceof InvalidationKeyWrapper)) {
                return false;
            }
            InvalidationKeyWrapper other = (InvalidationKeyWrapper) o;
            return this.actionClass.equals(other.actionClass) && this.key.equals(other.key);
        }
    }
}
