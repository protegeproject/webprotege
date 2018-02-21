package edu.stanford.bmir.protege.web.shared.search;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/12/2012
 */
public enum SearchType {

    EXACT_MATCH(false),

    EXACT_MATCH_IGNORE_CASE(true),

    SUB_STRING_MATCH_IGNORE_CASE(true);

    private boolean ignoreCase;

    SearchType(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public static SearchType getDefault() {
        return SUB_STRING_MATCH_IGNORE_CASE;
    }

    public boolean isCaseInsensitive() {
        return ignoreCase;
    }
}
