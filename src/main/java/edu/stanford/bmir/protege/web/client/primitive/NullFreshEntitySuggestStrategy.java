package edu.stanford.bmir.protege.web.client.primitive;

import edu.stanford.bmir.protege.web.client.library.suggest.EntitySuggestion;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Collections;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/03/2014
 */
public class NullFreshEntitySuggestStrategy implements FreshEntitySuggestStrategy {

    @Override
    public FreshEntitySuggestMode getMode() {
        return FreshEntitySuggestMode.DO_NOT_SUGGEST_CREATE_FRESH_ENTITIES;
    }

    @Override
    public List<EntitySuggestion> getSuggestions(String query, List<EntityType<?>> suggestedTypes) {
        return Collections.emptyList();
    }
}
