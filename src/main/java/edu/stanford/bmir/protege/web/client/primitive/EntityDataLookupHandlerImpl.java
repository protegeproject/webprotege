package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.entity.EntityLookupResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.EntityType;

import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/09/2013
 */
public class EntityDataLookupHandlerImpl implements EntityDataLookupHandler {

    private DispatchServiceManager dispatchServiceManager;

    @Inject
    public EntityDataLookupHandlerImpl(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void lookupEntity(String displayName, final EntityDataLookupContext lookupContext, final AsyncCallback<Optional<OWLEntityData>> callback) {
        final String trimmedContent = displayName.trim();
        final ProjectId projectId = lookupContext.getProjectId();
        final Set<EntityType<?>> allowedEntityTypes = lookupContext.getAllowedEntityTypes();
        if(allowedEntityTypes.isEmpty()) {
            callback.onSuccess(Optional.<OWLEntityData>absent());
        }
        final EntityLookupRequest entityLookupRequest = new EntityLookupRequest(trimmedContent, SearchType.EXACT_MATCH_IGNORE_CASE, 1, allowedEntityTypes);
        dispatchServiceManager.execute(new LookupEntitiesAction(projectId, entityLookupRequest), new AsyncCallback<LookupEntitiesResult>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(LookupEntitiesResult result) {
                List<EntityLookupResult> results = result.getEntityLookupResults();
                Optional<OWLEntityData> entityData = getMatchingEntity(results, trimmedContent, lookupContext);
                callback.onSuccess(entityData);
            }
        });
    }


    /**
     * Given a lookup result, gets the entity data which matches the current text in the editor.
     * @param result
     * @param text
     * @return
     */
    private Optional<OWLEntityData> getMatchingEntity(List<EntityLookupResult> result, String text, EntityDataLookupContext context) {
        if (result.isEmpty()) {
            return Optional.absent();
        }
        EntityLookupResult lookupResult = result.get(0);
        final OWLEntityData lookedUpEntityData = lookupResult.getOWLEntityData();
        EntityType<?> entityType = lookedUpEntityData.getEntity().getEntityType();
        if (lookedUpEntityData.getBrowserText().equalsIgnoreCase(text) && context.getAllowedEntityTypes().contains(entityType)) {
            return Optional.of(lookedUpEntityData);
        }
        else {
            return Optional.absent();
        }
    }

}
