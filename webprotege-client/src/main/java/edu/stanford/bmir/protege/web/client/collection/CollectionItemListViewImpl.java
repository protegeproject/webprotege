package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.list.ListBox;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jul 2017
 */
public class CollectionItemListViewImpl extends Composite implements CollectionItemListView {

    private final PaginatorPresenter paginatorPresenter;

    private SelectionChangedHandler selectionChangedHandler = () -> {};

    interface CollectionElementsListViewImplUiBinder extends UiBinder<HTMLPanel, CollectionItemListViewImpl> {

    }

    private static CollectionElementsListViewImplUiBinder ourUiBinder = GWT.create(
            CollectionElementsListViewImplUiBinder.class);

//    private SingleSelectionModel<CollectionItem> selectionModel = new SingleSelectionModel<>(CollectionItem::getId);

//    private ListDataProvider<CollectionItem> listDataProvider = new ListDataProvider<>();

    @UiField(provided = true)
    ListBox<CollectionItem, CollectionItem> elementList;

    @UiField(provided = true)
    PaginatorView paginatorView;

    @Inject
    public CollectionItemListViewImpl(PaginatorPresenter paginatorPresenter) {
        this.paginatorPresenter = paginatorPresenter;
        paginatorView = this.paginatorPresenter.getView();
        elementList = new ListBox<>();
        initWidget(ourUiBinder.createAndBindUi(this));
        elementList.addSelectionHandler(event -> {
            GWT.log("[CollectionItemListViewImpl] Selection changed");
            selectionChangedHandler.handleSelectionChanged();
        });
        elementList.setRenderer(new CollectionItemRenderer());
    }

    @Override
    public void setPageNumberChangedHandler(@Nonnull HasPagination.PageNumberChangedHandler pageNumberChangedHandler) {
        paginatorPresenter.setPageNumberChangedHandler(pageNumberChangedHandler);
    }

    @Override
    public int getPageNumber() {
        return paginatorPresenter.getPageNumber();
    }

    @Override
    public void setPageNumber(int pageNumber) {
        paginatorPresenter.setPageNumber(pageNumber);
    }

    @Override
    public void setPageCount(int pageCount) {
        paginatorPresenter.setPageCount(pageCount);
    }

    @Override
    public void setElementCount(long elementCount) {
        paginatorPresenter.setElementCount(elementCount);
    }

    @Override
    public void setSelectionChangedHandler(@Nonnull SelectionChangedHandler handler) {
        this.selectionChangedHandler = checkNotNull(handler);
    }

    @Override
    public void setSelection(@Nonnull CollectionItem selection) {
        GWT.log("[CollectionItemListViewImpl] setSelection() to " + selection);
        elementList.setSelection(selection);
    }

    @Override
    public void clearSelection() {
        elementList.clearSelection();
    }

    @Nonnull
    @Override
    public Optional<CollectionItem> getSelection() {
        return elementList.getFirstSelectedElement();
    }

    @Override
    public void setElements(@Nonnull List<CollectionItem> elements) {
        GWT.log("[CollectionItemListViewImpl] setElements");
        elementList.setListData(elements);
    }
}