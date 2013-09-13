package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestOracle;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2013
 */
public class PrimitiveDataEditorSuggestOracle extends SuggestOracle {

    private EntitySuggestOracle delegate;

    private Set<EntityType<?>> allowedPrimitiveTypes = new LinkedHashSet<EntityType<?>>();

    private PrimitiveDataEditorSuggestOracleMode mode = PrimitiveDataEditorSuggestOracleMode.DO_NOT_SUGGEST_CREATE_NEW_ENTITIES;

    public PrimitiveDataEditorSuggestOracle(EntitySuggestOracle delegate) {
        this.delegate = delegate;
        allowedPrimitiveTypes.add(EntityType.CLASS);
        allowedPrimitiveTypes.add(EntityType.NAMED_INDIVIDUAL);

    }

    public void setEntityTypes(Set<EntityType<?>> entityTypes) {
        allowedPrimitiveTypes.clear();
        allowedPrimitiveTypes.addAll(entityTypes);
        delegate.setEntityTypes(entityTypes);
    }

    public PrimitiveDataEditorSuggestOracleMode getMode() {
        return mode;
    }

    public void setMode(PrimitiveDataEditorSuggestOracleMode mode) {
        this.mode = mode;
    }

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
                    for(EntityType<?> allowedType : allowedPrimitiveTypes) {
                            OWLEntity entity = DataFactory.getFreshOWLEntity(allowedType, request.getQuery());
                            OWLEntityData entityData = DataFactory.getOWLEntityData(entity, request.getQuery());
                            suggestions.add(new EntitySuggestion(entityData, "<span class=\"new-keyword\">New</span> <span style=\"font-weight: bold;\">" + allowedType.getName() + "</span> named " + request.getQuery()));
                    }
                    callback.onSuggestionsReady(request, new Response(suggestions));
                }
                else {
                    callback.onSuggestionsReady(request, response);
                }

            }
        });
    }

    private boolean shouldOfferCreateSuggestions(Request request, List<EntitySuggestion> existingSuggestions) {
        if(mode != PrimitiveDataEditorSuggestOracleMode.SUGGEST_CREATE_NEW_ENTITIES) {
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
