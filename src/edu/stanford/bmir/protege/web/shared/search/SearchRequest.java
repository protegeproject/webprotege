package edu.stanford.bmir.protege.web.shared.search;


import com.google.gwt.regexp.shared.RegExp;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 * <p>
 *     Represents a search request as a regular expression.
 * </p>
 */
public class SearchRequest implements Serializable {

    private String regExpSource;

    private int limit;

    /**
     * For serialization purposes only
     */
    private SearchRequest() {
    }

    /**
     * Constructs a search request from a regular expression pattern.
     * @param searchPattern The pattern to match against.  Not {@code null}.
     * @throws NullPointerException if {@code searchPattern} is {@code null}.
     */
    public SearchRequest(RegExp searchPattern, int limit) {
        this.regExpSource = checkNotNull(searchPattern.getSource());
        this.limit = limit;
    }

    /**
     * Gets the search request as a regular expression pattern.
     * @return The search request as a regular expression pattern.
     */
    public RegExp getSearchPattern() {
        return RegExp.compile(regExpSource);
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public int hashCode() {
        return "SearchRequest".hashCode() + regExpSource.hashCode() + limit;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SearchRequest)) {
            return false;
        }
        SearchRequest other = (SearchRequest) obj;
        return this.regExpSource.equals(other.regExpSource) && this.limit == other.limit;
    }
}
