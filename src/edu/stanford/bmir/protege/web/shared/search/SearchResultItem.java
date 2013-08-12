package edu.stanford.bmir.protege.web.shared.search;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public abstract class SearchResultItem {

    /**
     * An HTML preview of the result
     */
    private String preview;

    /**
     * For serialization purposes only
     */
    protected SearchResultItem() {
    }

    protected SearchResultItem(String preview) {
        this.preview = preview;
    }

    public String getPreview() {
        return preview;
    }

    public abstract Serializable getSubject();
}
