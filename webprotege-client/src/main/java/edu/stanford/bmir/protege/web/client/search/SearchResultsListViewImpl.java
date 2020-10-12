package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

public class SearchResultsListViewImpl extends Composite implements SearchResultsListView {

    interface SearchResultsListViewImplUiBinder extends UiBinder<HTMLPanel, SearchResultsListViewImpl> {
    }

    private static SearchResultsListViewImplUiBinder ourUiBinder = GWT.create(SearchResultsListViewImplUiBinder.class);

    @UiField(provided = true)
    PaginatorView paginator;

    @UiField
    FlowPanel resultsListContainer;

    @UiField
    Label searchSummaryField;

    @Nonnull
    private final PaginatorPresenter paginatorPresenter;

    private int selectedIndex = -1;

    @Nonnull
    private AcceptKeyHandler acceptKeyHandler = () -> {};

    @Nonnull
    private SearchResultChosenHandler searchResultChosenHandler= result -> {};

    private long totalResultCount = 0;

    @Inject
    public SearchResultsListViewImpl(@Nonnull PaginatorPresenter paginatorPresenter) {
        this.paginator = paginatorPresenter.getView();
        this.paginatorPresenter = paginatorPresenter;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setPageCount(int pageCount) {
        paginatorPresenter.setPageCount(pageCount);
    }

    @Override
    public void setPageNumber(int pageNumber) {
        paginatorPresenter.setPageNumber(pageNumber);
    }

    @Override
    public int getPageNumber() {
        return paginatorPresenter.getPageNumber();
    }

    @Override
    public void setPageNumberChangedHandler(PageNumberChangedHandler handler) {
        paginatorPresenter.setPageNumberChangedHandler(handler);
    }

    @UiHandler("base")
    protected void handleDoubleClick(DoubleClickEvent event) {
        handleEventTarget(event.getClientY(), i -> acceptKeyHandler.handleAcceptKey());
    }

    @UiHandler("base")
    protected void handleClick(ClickEvent event) {
        handleEventTarget(event.getClientY(), this::setSelectedIndex);
    }

    private void handleEventTarget(int clientY, Consumer<Integer> consumer) {
        for(int i = 0; i < resultsListContainer.getWidgetCount(); i++) {
            Widget w = resultsListContainer.getWidget(i);
            int itemY = w.getAbsoluteTop();
            if(itemY < clientY && clientY < itemY + w.getOffsetHeight()) {
                consumer.accept(i);
                return;
            }
        }
    }


    @UiHandler("base")
    protected void handleBaseKeyDown(KeyDownEvent event) {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
            event.preventDefault();
            incrementSelectedIndex();
        }
        else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP) {
            event.preventDefault();
            decrementSelectedIndex();
        }
        else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            event.preventDefault();
            chooseSearchResult();
        }
    }

    private void clearCurrentSelectionBackground() {
        if(-1 < selectedIndex && selectedIndex < resultsListContainer.getWidgetCount()) {
            resultsListContainer.getWidget(selectedIndex).getElement().getStyle().clearBackgroundColor();
            resultsListContainer.getWidget(selectedIndex).getElement().getStyle().clearColor();
        }
    }

    @Override
    public void incrementSelectedIndex() {
        if (selectedIndex < resultsListContainer.getWidgetCount() - 1) {
            setSelectedIndex(selectedIndex + 1);
        }
    }

    @Override
    public void clearSearchResults() {
        resultsListContainer.clear();
        searchSummaryField.setText("");
    }

    @Override
    public int getSelectedSearchResultIndex() {
        return selectedIndex;
    }

    @Override
    public void setSearchResults(@Nonnull List<EntitySearchResultView> results) {
        resultsListContainer.clear();
        updateDisplayMessage();
        results.forEach(view -> resultsListContainer.add(view));
        if (resultsListContainer.getWidgetCount() > 0) {
            setSelectedIndex(0);
        }
        else {
            setSelectedIndex(-1);
        }
    }

    private void setSelectedIndex(int i) {
        clearCurrentSelectionBackground();
        selectedIndex = i;
        highlightSelectedIndex();
    }

    private void chooseSearchResult() {
        if(selectedIndex != -1) {
            searchResultChosenHandler.handleSearchResultChosen(selectedIndex);
            acceptKeyHandler.handleAcceptKey();
        }
    }

    @Override
    public void decrementSelectedIndex() {
        if (selectedIndex > 0) {
            setSelectedIndex(selectedIndex - 1);
        }
    }

    @Override
    public void setTotalResultCount(long totalElements) {
        paginatorPresenter.setElementCount(totalElements);
        this.totalResultCount = totalElements;
        updateDisplayMessage();
    }

    public void updateDisplayMessage() {
        String formattedResultsCount = NumberFormat.getDecimalFormat()
                                                   .format(totalResultCount);
        searchSummaryField.setText("Displaying " + resultsListContainer.getWidgetCount() + " of " + formattedResultsCount + " results");
    }

    private void highlightSelectedIndex() {
        if (-1 < selectedIndex && selectedIndex < resultsListContainer.getWidgetCount()) {
            Element element = resultsListContainer.getWidget(selectedIndex).getElement();
            element.getStyle().setBackgroundColor("var(--selected-item--background-color)");
            element.getStyle().setColor("var(--selected-item--color)");
            element.scrollIntoView();
        }
    }

    @Override
    public int getPageSize() {
        return paginatorPresenter.getPageSize();
    }

    @Override
    public void setAcceptKeyHandler(AcceptKeyHandler acceptKey) {
        this.acceptKeyHandler = checkNotNull(acceptKey);
    }
}