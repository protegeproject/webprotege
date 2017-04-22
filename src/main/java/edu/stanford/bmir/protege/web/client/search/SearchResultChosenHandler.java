package edu.stanford.bmir.protege.web.client.search;

import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Apr 2017
 */
public interface SearchResultChosenHandler {

    void handleSearchResultChosen(OWLEntityData result);
}
