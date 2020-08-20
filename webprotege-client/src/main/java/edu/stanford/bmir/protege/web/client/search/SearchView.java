package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public interface SearchView extends HasBusy, IsWidget, HasInitialFocusable {

    String getSearchString();

    void setSearchStringChangedHandler(SearchStringChangedHandler handler);

    void clearEntitySearchResults();

    void setEntitySearchResults(@Nonnull List<EntitySearchResultView> resultView);

    void setSearchResultChosenHandler(SearchResultChosenHandler handler);

    int getSelectedSearchResultIndex();

    void setLangTagFilterVisible(boolean visible);

    AcceptsOneWidget getLangTagFilterContainer();

    void setPageCount(int pageCount);

    void setPageNumber(int pageNumber);

    int getPageNumber();

    void setPageNumberChangedHandler(HasPagination.PageNumberChangedHandler handler);

    void setTotalResultCount(long totalElements);

    void setSearchFilterVisible(boolean visible);

    @Nonnull
    AcceptsOneWidget getSearchFilterContainer();
}
