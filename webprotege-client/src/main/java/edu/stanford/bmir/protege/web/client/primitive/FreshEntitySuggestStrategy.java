package edu.stanford.bmir.protege.web.client.primitive;

import edu.stanford.bmir.protege.web.client.library.suggest.EntitySuggestion;
import org.semanticweb.owlapi.model.EntityType;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/03/2014
 */
public interface FreshEntitySuggestStrategy {

    FreshEntitySuggestMode getMode();

    List<EntitySuggestion> getSuggestions(String query, List<EntityType<?>> suggestedTypes);
}
