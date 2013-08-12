package edu.stanford.bmir.protege.web.shared.search;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class SearchResultGroup implements Serializable {

    private SearchType type;

    private SearchResultCategory category;

    /**
     * For serialization only
     */
    private SearchResultGroup() {
    }

    public SearchResultGroup(SearchType type, SearchResultCategory category) {
        this.type = checkNotNull(type);
        this.category = checkNotNull(category);
    }

    public SearchType getSearchType() {
        return type;
    }

    public SearchResultCategory getResultCategory() {
        return category;
    }

    @Override
    public int hashCode() {
        return "SearchResultGroup".hashCode() + type.hashCode() + category.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SearchResultGroup)) {
            return false;
        }
        SearchResultGroup other = (SearchResultGroup) obj;
        return this.type.equals(other.type) && this.category.equals(other.category);
    }
}
