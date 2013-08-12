package edu.stanford.bmir.protege.web.shared.search;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class PerformSearchResult implements Result {

    private SearchResultSet resultSet;

    /**
     * For serialization purposes only
     */
    private PerformSearchResult() {
    }

    public PerformSearchResult(SearchResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public SearchResultSet getResultSet() {
        return resultSet;
    }
}
