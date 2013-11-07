package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.EntityLookupServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.EntityLookupServiceResult;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequest;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequestType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/09/2013
 */
public class EntityDataLookupHandlerServiceAsyncImpl implements EntityDataLookupHandler {

    private EntityLookupServiceAsync lookupServiceAsync;

    public EntityDataLookupHandlerServiceAsyncImpl(EntityLookupServiceAsync lookupServiceAsync) {
        this.lookupServiceAsync = lookupServiceAsync;
    }

    @Override
    public void lookupEntity(String displayName, final EntityDataLookupContext lookupContext, final AsyncCallback<Optional<OWLEntityData>> callback) {

        final String trimmedContent = displayName.trim();

        final ProjectId projectId = lookupContext.getProjectId();
        final Set<EntityType<?>> allowedEntityTypes = lookupContext.getAllowedEntityTypes();
        final EntityLookupRequest entityLookupRequest = new EntityLookupRequest(trimmedContent, EntityLookupRequestType.EXACT_MATCH_IGNORE_CASE, 5, allowedEntityTypes);
        lookupServiceAsync.lookupEntities(projectId, entityLookupRequest, new AsyncCallback<List<EntityLookupServiceResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(List<EntityLookupServiceResult> result) {
                Optional<OWLEntityData> entityData = getMatchingEntity(result, trimmedContent, lookupContext);
                callback.onSuccess(entityData);
//                if (entityData.isPresent()) {
//                    callback.onSuccess(entityData.get());
//                    return;
//                }
////                if (context.isIRIAllowed() && isAbsoluteIRI(trimmedContent)) {
////                    IRIData iriData = new IRIData(IRI.create(trimmedContent));
////                    return;
////                }
////
////                if (context.isLiteralAllowed()) {
////                    OWLLiteralData literalData = parseLiteralData(trimmedContent, lang);
////                    handleSuccess(literalData, callback);
////                    return;
////                }
//
//                // TODO: Not matched allowed type
//                callback.parsingFailure();
            }
        });
    }


    /**
     * Given a lookup result, gets the entity data which matches the current text in the editor.
     * @param result
     * @param text
     * @return
     */
    private Optional<OWLEntityData> getMatchingEntity(List<EntityLookupServiceResult> result, String text, EntityDataLookupContext context) {
        if (result.isEmpty()) {
            return Optional.absent();
        }
        EntityLookupServiceResult lookupResult = result.get(0);
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
