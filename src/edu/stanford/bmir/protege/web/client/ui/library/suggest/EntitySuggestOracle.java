package edu.stanford.bmir.protege.web.client.ui.library.suggest;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.EntityType;

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

    final Set<EntityType<?>> entityTypes = new HashSet<EntityType<?>>();

    public EntitySuggestOracle(ProjectId projectId, int suggestLimit, EntityType<?> ... entityMatchTypes) {
        this.projectId = projectId;
        this.suggestLimit = suggestLimit;
        entityTypes.addAll(Arrays.asList(entityMatchTypes));
    }

    public EntitySuggestOracle(ProjectId projectId) {
        this(projectId, DEFAULT_SUGGEST_LIMIT);
    }

    public Set<EntityType<?>> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(Set<EntityType<?>> entityTypes) {
        this.entityTypes.clear();
        this.entityTypes.addAll(entityTypes);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        DispatchServiceManager.get().execute(new LookupEntitiesAction(projectId, new EntityLookupRequest(request.getQuery(), SearchType.getDefault(), suggestLimit, entityTypes)), new AsyncCallback<LookupEntitiesResult>() {
            @Override
            public void onFailure(Throwable caught) {
                MessageBox.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(LookupEntitiesResult result) {
                List<EntitySuggestion> suggestions = new ArrayList<EntitySuggestion>();
                for (final EntityLookupResult entity : result.getEntityLookupResults()) {
                    suggestions.add(new EntitySuggestion(entity.getOWLEntityData(), entity.getDisplayText()));
                }
                callback.onSuggestionsReady(request, new Response(suggestions));
            }
        });
    }

    @Override
    public boolean isDisplayStringHTML() {
        return true;
    }


//    private List<EntitySuggestion> getEntityCreationSuggestions(Request request) {
//        List<EntitySuggestion> suggestions = new ArrayList<EntitySuggestion>();
//
//
//        if(entityTypes.contains(EntityLookupRequestEntityMatchType.MATCH_CLASSES)) {
//            OWLClass cls = DataFactory.getOWLClass("");
//            OWLClassData data = new OWLClassData(cls, request.getQuery());
//            suggestions.add(new EntitySuggestion(data, "Create class called " + request.getQuery()));
//        }
//
//        if(entityTypes.contains(EntityLookupRequestEntityMatchType.MATCH_OBJECT_PROPERTIES)) {
//            OWLObjectProperty property = DataFactory.getOWLObjectProperty("");
//            OWLEntityData entityData = new OWLObjectPropertyData(property, request.getQuery());
//            suggestions.add(new EntitySuggestion(entityData, "Create object property called " + request.getQuery()));
//        }
//
//        if(entityTypes.contains(EntityLookupRequestEntityMatchType.MATCH_DATA_PROPERTIES)) {
//            OWLDataProperty property = DataFactory.getOWLDataProperty("");
//            OWLDataPropertyData entityData = new OWLDataPropertyData(property, request.getQuery());
//            suggestions.add(new EntitySuggestion(entityData, "Create data property called " + request.getQuery()));
//        }
//
//
//
//        if(entityTypes.contains(EntityLookupRequestEntityMatchType.MATCH_ANNOTATION_PROPERTIES)) {
//            OWLAnnotationProperty property = DataFactory.getOWLAnnotationProperty("");
//            OWLAnnotationPropertyData entityData = new OWLAnnotationPropertyData(property, request.getQuery());
//            suggestions.add(new EntitySuggestion(entityData, "Create annotation property called " + request.getQuery()));
//        }
//
//
//
//        if(entityTypes.contains(EntityLookupRequestEntityMatchType.MATCH_NAMED_INDIVIDUALS)) {
//            OWLNamedIndividual property = DataFactory.getOWLNamedIndividual("");
//            OWLEntityData entityData = new OWLNamedIndividualData(property, request.getQuery());
//            suggestions.add(new EntitySuggestion(entityData, "Create individual called " + request.getQuery()));
//        }
//
//        return suggestions;
//
//
//    }
}
