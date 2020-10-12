package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasAcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.pagination.HasPages;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-06
 */
public interface SearchResultsListView extends IsWidget, HasPagination, HasAcceptKeyHandler {

    void setSearchResults(@Nonnull List<EntitySearchResultView> results);

    void decrementSelectedIndex();

    void setTotalResultCount(long totalElements);

    void incrementSelectedIndex();

    void clearSearchResults();

    int getSelectedSearchResultIndex();
}
