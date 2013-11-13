package edu.stanford.bmir.protege.web.shared.entity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/12/2012
 */
public enum EntitySearchType {

    EXACT_MATCH(false),

    EXACT_MATCH_IGNORE_CASE(true),

    SUB_STRING_MATCH_IGNORE_CASE(true);

    private boolean ignoreCase;

    private EntitySearchType(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public static EntitySearchType getDefault() {
        return SUB_STRING_MATCH_IGNORE_CASE;
    }

    public boolean isCaseInsensitive() {
        return ignoreCase;
    }
}
