package edu.stanford.bmir.protege.web.shared.search;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 * <p>
 *     Specifies a dimension along which a search should be performed.
 * </p>
 */
public class SearchFacet implements Serializable {


    public static transient SearchFacet DISPLAY_NAME = new SearchFacet("Display name");

    public static transient SearchFacet IRI = new SearchFacet("IRI");

    public static transient SearchFacet ANNOTATION_VALUE = new SearchFacet("Annotation values");

    private String facetName;

    /**
     * Constructs the facet using the facet name.
     * @param facetName The facet name.  Not {@code null}.
     * @throws NullPointerException if {@code facetName} is null.
     */
    public SearchFacet(String facetName) {
        this.facetName = facetName;
    }

    public String getFacetName() {
        return facetName;
    }

    @Override
    public int hashCode() {
        return "SearchFacet".hashCode() + facetName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SearchFacet)) {
            return false;
        }
        SearchFacet other = (SearchFacet) obj;
        return this.facetName.equals(other.facetName);
    }
}
