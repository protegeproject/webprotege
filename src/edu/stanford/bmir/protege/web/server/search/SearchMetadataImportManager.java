package edu.stanford.bmir.protege.web.server.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class SearchMetadataImportManager {

    public List<SearchMetadataImporter> getImporters() {
        List<SearchMetadataImporter> importers = new ArrayList<SearchMetadataImporter>();
        importers.add(new DefaultSearchMetadataImporter());
        return importers;
    }
}
