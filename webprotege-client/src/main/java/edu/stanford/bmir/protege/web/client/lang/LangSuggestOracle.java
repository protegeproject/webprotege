package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.shared.lang.LanguageCode;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/04/16
 */
public class LangSuggestOracle extends SuggestOracle {

    private final List<LanguageCode> codes;

    @Inject
    public LangSuggestOracle(@LanguageCodes List<LanguageCode> codes) {
        this.codes = codes;
    }

    @Override
    public void requestSuggestions(Request request, Callback callback) {
        String query = request.getQuery().toLowerCase();
        List<LangCodeSuggestion> suggestions = new ArrayList<>();
        for(LanguageCode code : codes) {
            if(code.getLang().contains(query) || code.getName().toLowerCase().contains(query)) {
                suggestions.add(new LangCodeSuggestion(code, query));
            }
        }
        Collections.sort(suggestions);
        if(suggestions.size() > 10) {
            suggestions = suggestions.subList(0, 10);
        }
        callback.onSuggestionsReady(request, new Response(suggestions));
    }

    @Override
    public boolean isDisplayStringHTML() {
        return true;
    }
}
