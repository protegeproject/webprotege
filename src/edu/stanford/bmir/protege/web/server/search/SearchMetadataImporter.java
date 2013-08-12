package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.search.SearchType;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public interface SearchMetadataImporter {

    SearchMetadataDB getSearchMetadata(OWLAPIProject project, Set<SearchType> types);
}
