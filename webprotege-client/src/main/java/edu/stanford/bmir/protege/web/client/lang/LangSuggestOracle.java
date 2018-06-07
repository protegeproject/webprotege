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
            String lang = code.getLang().toLowerCase();
            int langIndex = lang.indexOf(query);
            if(langIndex != -1 && isSubLangStart(lang, langIndex)) {
                suggestions.add(new LangCodeSuggestion(code, query));
            }
            else {
                String name = code.getName();
                int index = name.toLowerCase().indexOf(query);
                if(index != -1 && Character.isUpperCase(name.charAt(index))) {
                    suggestions.add(new LangCodeSuggestion(code, query));
                }
            }
        }
        Collections.sort(suggestions);
        if(suggestions.size() > 10) {
            suggestions = suggestions.subList(0, 10);
        }
        callback.onSuggestionsReady(request, new Response(suggestions));
    }

    private boolean isSubLangStart(String lang, int langIndex) {
        return langIndex != -1
                && (langIndex == 0 || langIndex > 0
                && lang.charAt(langIndex - 1) == '-');
    }

    @Override
    public boolean isDisplayStringHTML() {
        return true;
    }
}
