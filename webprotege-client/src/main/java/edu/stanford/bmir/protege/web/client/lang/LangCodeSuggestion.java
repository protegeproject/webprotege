package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.shared.lang.LanguageCode;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/04/16
 */
public class LangCodeSuggestion implements SuggestOracle.Suggestion, Comparable<LangCodeSuggestion> {

    private final LanguageCode code;

    private final String query;

    public LangCodeSuggestion(LanguageCode code, String query) {
        this.code = code;
        this.query = query.toLowerCase();
    }

    @Override
    public String getDisplayString() {
        String base = code.getLang() + " (" + code.getName() + ")";
        int index = base.toLowerCase().indexOf(query.toLowerCase());
        String displayString;
        if(index != -1) {
            displayString = base.substring(0, index)
                            + "<span style=\"font-weight: bold;\">"
                            + base.substring(index, index + query.length())
                            + "</span>"
                            + base.substring(index + query.length());
        }
        else {
            displayString = base;
        }
        return displayString;
    }

    @Override
    public String getReplacementString() {
        return code.getLang();
    }

    private int getLangMatchIndex() {
        return code.getLang().indexOf(query);
    }

    private int getNameMatchIndex() {
        return code.getName().toLowerCase().indexOf(query);
    }

    @Override
    public int compareTo(LangCodeSuggestion o) {
        if(this.getLangMatchIndex() == 0) {
            if(o.getLangMatchIndex() == 0) {
                return this.code.getLang().compareTo(o.code.getLang());
            }
            else {
                return -1;
            }
        }
        else {
            if(o.getNameMatchIndex() == 0) {
                return 1;
            }
            else {
                return this.getNameMatchIndex() - o.getNameMatchIndex();
            }
        }
    }
}
