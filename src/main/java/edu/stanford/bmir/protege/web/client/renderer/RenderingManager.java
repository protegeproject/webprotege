package edu.stanford.bmir.protege.web.client.renderer;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityDataResult;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/11/2012
 */
public class RenderingManager {

    public static final RenderingManager INSTANCE = new RenderingManager();

    public static final int MAX_CACHE_SIZE = 1000;

    private final Cache<OWLEntity,OWLEntityData> cache;


    private RenderingManager() {
        cache = CacheBuilder.newBuilder().maximumSize(MAX_CACHE_SIZE).build();
    }

    public static RenderingManager getManager() {
        return INSTANCE;
    }


    public void update(OWLEntity entity) {
        cache.invalidate(entity);
    }

    public void registerEntityData(Collection<OWLEntityData> entityData) {
        for(OWLEntityData ed : entityData) {
            cache.put(ed.getEntity(), ed);
        }
    }

    public void execute(GetEntityDataAction action, final AsyncCallback<GetEntityDataResult> async) {
        Set<OWLEntity> toFetch = new HashSet<>();
        final Map<OWLEntity, OWLEntityData> cachedResult = new HashMap<OWLEntity, OWLEntityData>();
        for(OWLEntity entity : action.getEntities()) {
            OWLEntityData entityData = cache.getIfPresent(entity);
            if(entityData == null) {
                toFetch.add(entity);
            }
            else {
                cachedResult.put(entity, entityData);
            }
        }
        if(toFetch.isEmpty()) {
            GWT.log("[RenderingManager]  Using cached results");
            async.onSuccess(new GetEntityDataResult(ImmutableMap.copyOf(cachedResult)));
        }
        else {
            GWT.log("[RenderingManager]: Getting results from server");
            DispatchServiceManager.get().execute(new GetEntityDataAction(action.getProjectId(), ImmutableSet.copyOf(toFetch)), new DispatchServiceCallback<GetEntityDataResult>() {
                @Override
                public void handleSuccess(GetEntityDataResult result) {
                    Map<OWLEntity, OWLEntityData> r = new HashMap<OWLEntity, OWLEntityData>();
                    r.putAll(result.getEntityDataMap());
                    cache.putAll(result.getEntityDataMap());
                    r.putAll(cachedResult);
                    async.onSuccess(new GetEntityDataResult(ImmutableMap.copyOf(r)));
                }
            });
        }
    }


}
