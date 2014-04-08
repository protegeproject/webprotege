package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/03/2014
 */
public class SimpleFreshEntitySuggestStrategy implements FreshEntitySuggestStrategy {

    public SimpleFreshEntitySuggestStrategy() {
    }

    @Override
    public FreshEntitySuggestMode getMode() {
        return FreshEntitySuggestMode.SUGGEST_CREATE_FRESH_ENTITIES;
    }

    @Override
    public List<EntitySuggestion> getSuggestions(String query, List<EntityType<?>> suggestedTypes) {
        List<EntitySuggestion> suggestions = Lists.newArrayList();
        for(EntityType<?> allowedType : suggestedTypes) {
                OWLEntity entity = DataFactory.getFreshOWLEntity(allowedType, query);
                OWLEntityData entityData = DataFactory.getOWLEntityData(entity, query);
                suggestions.add(new EntitySuggestion(entityData, formatSuggestText(query, allowedType)));
        }
        return suggestions;
    }

    private String formatSuggestText(String query, EntityType<?> allowedType) {
        return "<span class=\"new-keyword\">New</span> <span style=\"font-weight: bold;\">" + allowedType.getPrintName() + "</span> named " + query;
    }
}
