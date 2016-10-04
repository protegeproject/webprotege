package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import javax.inject.Inject;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/09/2013
 */
public class EntityDataLookupHandlerImpl implements EntityDataLookupHandler {

    private final DispatchServiceManager dispatchServiceManager;

    private final FreshEntitiesHandler freshEntitiesHandler;

    private final ProjectId projectId;

    @Inject
    public EntityDataLookupHandlerImpl(ProjectId projectId, FreshEntitiesHandler freshEntitiesHandler, DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.freshEntitiesHandler = freshEntitiesHandler;
        this.projectId = projectId;
    }

    @Override
    public void lookupEntity(String displayName, final Set<EntityType<?>> allowedEntityTypes, final AsyncCallback<Optional<OWLEntityData>> callback) {
        final String trimmedContent = displayName.trim();
        if(allowedEntityTypes.isEmpty()) {
            callback.onSuccess(Optional.<OWLEntityData>absent());
            return;
        }
        if(freshEntitiesHandler.isRegisteredFreshEntity(displayName)) {
            Optional<OWLEntity> freshEntity = freshEntitiesHandler.getRegisteredFreshEntity(displayName);
            if (freshEntity.isPresent()) {
                OWLEntityData entityData = DataFactory.getOWLEntityData(freshEntity.get(), displayName);
                callback.onSuccess(Optional.of(entityData));
                return;
            }
        }
        final EntityLookupRequest entityLookupRequest = new EntityLookupRequest(trimmedContent, SearchType.EXACT_MATCH_IGNORE_CASE, 1, allowedEntityTypes);
        dispatchServiceManager.execute(new LookupEntitiesAction(projectId, entityLookupRequest), new DispatchServiceCallback<LookupEntitiesResult>() {

            @Override
            public void handleExecutionException(Throwable cause) {
                callback.onFailure(cause);
            }

            @Override
            public void handleSuccess(LookupEntitiesResult result) {
                List<EntityLookupResult> results = result.getEntityLookupResults();
                Optional<OWLEntityData> entityData = getMatchingEntity(results, trimmedContent, projectId, allowedEntityTypes);
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
    private Optional<OWLEntityData> getMatchingEntity(List<EntityLookupResult> result, String text, ProjectId projectId, Set<EntityType<?>> allowedEntityTypes) {
        if (result.isEmpty()) {
            return Optional.absent();
        }
        EntityLookupResult lookupResult = result.get(0);
        final OWLEntityData lookedUpEntityData = lookupResult.getOWLEntityData();
        EntityType<?> entityType = lookedUpEntityData.getEntity().getEntityType();
        if (lookedUpEntityData.getBrowserText().equalsIgnoreCase(text) && allowedEntityTypes.contains(entityType)) {
            return Optional.of(lookedUpEntityData);
        }
        else {
            return Optional.absent();
        }
    }

}
