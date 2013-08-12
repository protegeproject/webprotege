package edu.stanford.bmir.protege.web.server.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class SearchMetadataDB {

    private List<SearchMetadata> results = new ArrayList<SearchMetadata>();

    public void addResult(SearchMetadata searchMetadata) {
        results.add(searchMetadata);
    }

    public List<SearchMetadata> getResults() {
        return Collections.unmodifiableList(results);
    }
}
