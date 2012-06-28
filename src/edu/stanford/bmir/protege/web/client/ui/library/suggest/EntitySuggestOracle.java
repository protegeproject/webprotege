package edu.stanford.bmir.protege.web.client.ui.library.suggest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.rpc.EntityLookupService;
import edu.stanford.bmir.protege.web.client.rpc.EntityLookupServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.EntityLookupServiceResult;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequest;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequestEntityMatchType;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class EntitySuggestOracle extends SuggestOracle {

    public static final int DEFAULT_SUGGEST_LIMIT = 30;

    private int suggestLimit = DEFAULT_SUGGEST_LIMIT;

    final private ProjectId projectId;

    final private EntityLookupServiceAsync service = GWT.create(EntityLookupService.class);
    
    final Set<EntityLookupRequestEntityMatchType> entityTypes = new HashSet<EntityLookupRequestEntityMatchType>();

    public EntitySuggestOracle(ProjectId projectId, int suggestLimit, EntityLookupRequestEntityMatchType ... entityMatchTypes) {
        this.projectId = projectId;
        this.suggestLimit = suggestLimit;
        entityTypes.addAll(Arrays.asList(entityMatchTypes));
    }

    public EntitySuggestOracle(ProjectId projectId) {
        this(projectId, DEFAULT_SUGGEST_LIMIT);
    }

    public Set<EntityLookupRequestEntityMatchType> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(Set<EntityLookupRequestEntityMatchType> entityTypes) {
        this.entityTypes.clear();
        this.entityTypes.addAll(entityTypes);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        service.lookupEntities(getProjectId(), new EntityLookupRequest(request.getQuery(), suggestLimit, entityTypes), new AsyncCallback<List<EntityLookupServiceResult>>() {
            public void onFailure(Throwable caught) {
                MessageBox.alert(caught.getMessage());
            }

            public void onSuccess(List<EntityLookupServiceResult> result) {
                List<EntitySuggestion> suggestions = new ArrayList<EntitySuggestion>();
                for (final EntityLookupServiceResult entity : result) {
                    suggestions.add(new EntitySuggestion(entity.getVisualEntity(), entity.getDisplayText()));
                }
                callback.onSuggestionsReady(request, new Response(suggestions));
            }
        });
    }

    @Override
    public boolean isDisplayStringHTML() {
        return true;
    }
}
