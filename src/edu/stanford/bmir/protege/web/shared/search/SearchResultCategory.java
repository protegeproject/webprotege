package edu.stanford.bmir.protege.web.shared.search;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class SearchResultCategory implements Serializable {

    private String category;

    /**
     * For serialization purposes only
     */
    private SearchResultCategory() {
    }

    public SearchResultCategory(String category) {
        this.category = checkNotNull(category);
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int hashCode() {
        return "SearchResultCategory".hashCode() + category.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SearchResultCategory)) {
            return false;
        }
        SearchResultCategory other = (SearchResultCategory) obj;
        return this.category.equals(other.category);
    }
}
