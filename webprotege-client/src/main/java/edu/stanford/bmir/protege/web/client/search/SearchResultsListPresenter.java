package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination.PageNumberChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-06
 */
public class SearchResultsListPresenter {

    private SearchResultChosenHandler resultChosenHandler = (result) -> {};

    @Nonnull
    private final SearchResultsListView view;

    @Nonnull
    private final EntitySearchResultPresenterFactory resultPresenterFactory;

    @Nonnull
    private final List<EntitySearchResultPresenter> resultPresenters = new ArrayList<>();

    @Nonnull
    private PageNumberChangedHandler pageNumberChangedHandler = pageNumber -> {};

    @Inject
    public SearchResultsListPresenter(@Nonnull SearchResultsListView view,
                                      @Nonnull EntitySearchResultPresenterFactory resultPresenterFactory) {
        this.view = checkNotNull(view);
        this.resultPresenterFactory = resultPresenterFactory;
        this.view.setPageNumberChangedHandler(this::handlePageNumberChanged);
    }

    public void setPageNumberChangedHandler(@Nonnull PageNumberChangedHandler pageNumberChangedHandler) {
        this.pageNumberChangedHandler = checkNotNull(pageNumberChangedHandler);
    }

    private void handlePageNumberChanged(int pageNumber) {
        this.pageNumberChangedHandler.handlePageNumberChanged(pageNumber);
    }

    public void clearSearchResults() {
        resultPresenters.clear();
        view.clearSearchResults();
    }


    public void displaySearchResult(Page<EntitySearchResult> resultsPage) {
        resultPresenters.clear();
        resultsPage.getPageElements()
                   .stream()
                   .map(r -> {
                       EntitySearchResultPresenter presenter = resultPresenterFactory.create(r);
                       presenter.start();
                       return presenter;
                   })
                   .forEach(resultPresenters::add);
        ImmutableList<EntitySearchResultView> resultViews = resultPresenters.stream()
                                                                            .map(EntitySearchResultPresenter::getView)
                                                                            .collect(toImmutableList());
        view.setSearchResults(resultViews);
        view.setPageCount(resultsPage.getPageCount());
        view.setPageNumber(resultsPage.getPageNumber());
        view.setTotalResultCount(resultsPage.getTotalElements());
    }

    public void setPageNumber(int pageNumber) {
        view.setPageNumber(pageNumber);
    }

    public void setSearchResultChosenHandler(SearchResultChosenHandler handler) {
        this.resultChosenHandler = checkNotNull(handler);
    }


    public Optional<OWLEntityData> getSelectedSearchResult() {
        int selIndex = view.getSelectedSearchResultIndex();
        if(selIndex == -1) {
            return Optional.empty();
        }
        EntitySearchResultPresenter resultPresenter = resultPresenters.get(selIndex);
        return Optional.of(resultPresenter.getEntity().getEntityData());
    }

    public int getPageNumber() {
        return view.getPageNumber();
    }

    public void incrementSelection() {
        view.incrementSelectedIndex();
    }

    public void decrementSelection() {
        view.decrementSelectedIndex();
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }
}


