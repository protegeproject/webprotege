package edu.stanford.bmir.protege.web.client.rpc;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/11/2012
 */
public class RenderingServiceManager implements RenderingServiceAsync {

    public static final RenderingServiceManager INSTANCE = new RenderingServiceManager();

    public static final int MAX_CACHE_SIZE = 1000;

    private RenderingServiceAsync delegate = GWT.create(RenderingService.class);

//    private Map<OWLEntity, OWLEntityData> renderingCache = new HashMap<OWLEntity, OWLEntityData>();

    private final Cache<OWLEntity,OWLEntityData> cache;


    private RenderingServiceManager() {
        cache = CacheBuilder.newBuilder().maximumSize(MAX_CACHE_SIZE).build();
    }

    public static RenderingServiceManager getManager() {
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

    @Override
    public void execute(GetRendering request, final AsyncCallback<GetRenderingResponse> async) {
        Set<OWLEntity> toFetch = new HashSet<OWLEntity>();
        final Map<OWLEntity, OWLEntityData> cachedResult = new HashMap<OWLEntity, OWLEntityData>();
        for(OWLEntity entity : request.getEntities()) {
            OWLEntityData entityData = cache.getIfPresent(entity);
            if(entityData == null) {
                toFetch.add(entity);
            }
            else {
                cachedResult.put(entity, entityData);
            }
        }
        if(toFetch.isEmpty()) {
            GWT.log("RenderingSeviceManager:  Using cached results");
            async.onSuccess(new GetRenderingResponse(cachedResult));
        }
        else {
            GWT.log("RenderingSeviceManager: Getting results from server");
            delegate.execute(new GetRendering(request.getProjectId(), toFetch), new AsyncCallback<GetRenderingResponse>() {
                @Override
                public void onFailure(Throwable caught) {
                    async.onFailure(caught);
                }

                /**
                 * Called when an asynchronous call completes successfully.
                 * @param response the return value of the remote produced call
                 */
                @Override
                public void onSuccess(GetRenderingResponse response) {
//                    renderingCache.putAll(response.getMap());
                    Map<OWLEntity, OWLEntityData> result = new HashMap<OWLEntity, OWLEntityData>();
                    result.putAll(response.getMap());
                    cache.putAll(response.getMap());
                    result.putAll(cachedResult);
                    async.onSuccess(new GetRenderingResponse(result));
                }
            });
        }
    }


}
