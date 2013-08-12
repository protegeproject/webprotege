package edu.stanford.bmir.protege.web.server.search;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public interface SearchResultHandler {

    void searchFinished(List<SearchResult> searchResults);
}
