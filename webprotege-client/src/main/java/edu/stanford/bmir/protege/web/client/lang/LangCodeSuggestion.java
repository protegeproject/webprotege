package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.shared.lang.LanguageCode;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/04/16
 */
public class LangCodeSuggestion implements SuggestOracle.Suggestion, Comparable<LangCodeSuggestion> {

    private static final int BEFORE = -1;

    private static final int AFTER = 1;

    private final LanguageCode code;

    private final String query;

    private static final Function<LangCodeSuggestion, Integer> BY_MATCHED_LANG = s -> {
        int langMatchIndex = s.getLangMatchIndex();
        if (langMatchIndex == -1) {
            return Integer.MAX_VALUE;
        }
        else {
            return langMatchIndex;
        }
    };

    private static final Function<LangCodeSuggestion, Integer> BY_MATCHED_NAME = s -> {
        int nameMatchIndex = s.getNameMatchIndex();
        if (nameMatchIndex == -1) {
            return Integer.MAX_VALUE;
        }
        else {
            return nameMatchIndex;
        }
    };

    private static final Function<LangCodeSuggestion, String> BY_DISPLAY_NAME = s -> s.getPlainRendering().toLowerCase();

    private static final Comparator<LangCodeSuggestion> LANG_CODE_SUGGESTION_COMPARATOR = Comparator.comparing(BY_MATCHED_LANG)
                                                                                                    .thenComparing(BY_MATCHED_NAME)
                                                                                                    .thenComparing(BY_DISPLAY_NAME);

    public LangCodeSuggestion(LanguageCode code, String query) {
        this.code = code;
        this.query = query.toLowerCase();
    }

    public String getName() {
        return code.getName();
    }

    public String getPlainRendering() {
        return code.getLang() + " (" + code.getName() + ")";
    }

    @Override
    public String getDisplayString() {
        String base = code.getLang() + " (" + code.getName() + ")";
        int index = base.toLowerCase().indexOf(query.toLowerCase());
        String displayString;
        if(index != -1) {
            displayString = "<div>" + base.substring(0, index)
                            + "<span class=\"web-protege-entity-match-substring\">"
                            + base.substring(index, index + query.length())
                            + "</span>"
                            + base.substring(index + query.length()) + "</div>";
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
        return code.getLang().toLowerCase().indexOf(query.toLowerCase());
    }

    private int getNameMatchIndex() {
        return code.getName().toLowerCase().indexOf(query.toLowerCase());
    }

    @Override
    public int compareTo(@Nonnull LangCodeSuggestion o) {
        return LANG_CODE_SUGGESTION_COMPARATOR.compare(this, o);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LangCodeSuggestion)) {
            return false;
        }
        LangCodeSuggestion other = (LangCodeSuggestion) obj;
        return this.code.equals(other.code);
    }
}
