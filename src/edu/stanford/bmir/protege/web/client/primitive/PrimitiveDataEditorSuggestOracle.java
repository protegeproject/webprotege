package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestOracle;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2013
 */
public class PrimitiveDataEditorSuggestOracle extends SuggestOracle {

    private EntitySuggestOracle delegate;

    private Set<EntityType<?>> allowedPrimitiveTypes = new LinkedHashSet<EntityType<?>>();

//    private FreshEntitySuggestMode mode;

    private FreshEntitySuggestStrategy freshEntityStrategy
            = new NullFreshEntitySuggestStrategy();

    @Inject
    public PrimitiveDataEditorSuggestOracle(EntitySuggestOracle delegate,
                                            @EntitySuggestOracleSuggestMode FreshEntitySuggestMode mode) {
        this.delegate = delegate;
//        this.mode = mode;
        allowedPrimitiveTypes.add(EntityType.CLASS);
        allowedPrimitiveTypes.add(EntityType.NAMED_INDIVIDUAL);
    }

    public void setFreshEntityStrategy(FreshEntitySuggestStrategy strategy) {
        this.freshEntityStrategy = strategy;
    }

    public void setEntityTypes(Set<EntityType<?>> entityTypes) {
        allowedPrimitiveTypes.clear();
        allowedPrimitiveTypes.addAll(entityTypes);
        delegate.setEntityTypes(entityTypes);
    }

//    public FreshEntitySuggestMode getMode() {
//        return mode;
//    }

//    public void setMode(FreshEntitySuggestMode mode) {
//        this.mode = mode;
//    }

    /**
     * Generate a {@link com.google.gwt.user.client.ui.SuggestOracle.Response} based on a specific {@link
     * com.google.gwt.user.client.ui.SuggestOracle.Request}. After the
     * {@link com.google.gwt.user.client.ui.SuggestOracle.Response} is created, it is passed into
     * {@link com.google.gwt.user.client.ui.SuggestOracle.Callback#onSuggestionsReady(com.google.gwt.user.client.ui.SuggestOracle.Request,
     * com.google.gwt.user.client.ui.SuggestOracle.Response)}.
     * @param request the request
     * @param callback the callback to use for the response
     */
    @Override
    public void requestSuggestions(Request request, final Callback callback) {
        delegate.requestSuggestions(request, new Callback() {
            @Override
            public void onSuggestionsReady(Request request, Response response) {
                List<EntitySuggestion> suggestions = (List<EntitySuggestion>) response.getSuggestions();
                if (shouldOfferCreateSuggestions(request, suggestions)) {
                    suggestions.addAll(freshEntityStrategy.getSuggestions(request.getQuery(),
                            getSuggestedTypesForQuery(request.getQuery())));
                    callback.onSuggestionsReady(request, new Response(suggestions));
                }
                else {
                    callback.onSuggestionsReady(request, response);
                }

            }
        });
    }

    private List<EntityType<?>> getSuggestedTypesForQuery(String query) {
        if(query.length() == 0) {
            return Lists.newArrayList(allowedPrimitiveTypes);
        }
        List<EntityType<?>> result = Lists.newArrayList(allowedPrimitiveTypes);
        if(Character.isLowerCase(query.charAt(0))) {
            if(result.remove(EntityType.NAMED_INDIVIDUAL)) {
                result.add(0, EntityType.NAMED_INDIVIDUAL);
            }
        }
        else {
            if(result.remove(EntityType.CLASS)) {
                result.add(0, EntityType.CLASS);
            }
        }
        return result;
    }

    private boolean shouldOfferCreateSuggestions(Request request, List<EntitySuggestion> existingSuggestions) {
        if(freshEntityStrategy.getMode() != FreshEntitySuggestMode.SUGGEST_CREATE_FRESH_ENTITIES) {
            return false;
        }
        if(canParseAsNumericLiteral(request)) {
            return false;
        }
        for(EntitySuggestion existingSuggestion : existingSuggestions) {
            if(existingSuggestion.getEntity().getBrowserText().equalsIgnoreCase(request.getQuery())) {
                return false;
            }
        }
        return true;
    }

    private boolean canParseAsNumericLiteral(Request request) {
        OWLLiteral lit = DataFactory.parseLiteral(request.getQuery(), Optional.<String>absent());
        return !(lit.getDatatype().isString() || lit.getDatatype().isRDFPlainLiteral());
    }

    /**
     * Should {@link com.google.gwt.user.client.ui.SuggestOracle.Suggestion} display strings be treated as HTML? If true,
     * this
     * all suggestions' display strings will be interpreted as HTML, otherwise as
     * text.
     * @return by default, returns false
     */
    @Override
    public boolean isDisplayStringHTML() {
        return delegate.isDisplayStringHTML();
    }

}
