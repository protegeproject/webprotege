package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import edu.stanford.bmir.protege.web.client.list.ListBox;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.resources.WebProtegeCellListResources;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;

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
public class CollectionElementsListViewImpl extends Composite implements CollectionElementsListView {

    private final PaginatorPresenter paginatorPresenter;

    private SelectionChangedHandler selectionChangedHandler = () -> {};

    interface CollectionElementsListViewImplUiBinder extends UiBinder<HTMLPanel, CollectionElementsListViewImpl> {

    }

    private static CollectionElementsListViewImplUiBinder ourUiBinder = GWT.create(
            CollectionElementsListViewImplUiBinder.class);

//    private SingleSelectionModel<CollectionElementId> selectionModel = new SingleSelectionModel<>(CollectionElementId::getId);

//    private ListDataProvider<CollectionElementId> listDataProvider = new ListDataProvider<>();

    @UiField(provided = true)
    ListBox<CollectionElementId, CollectionElementId> elementList;

    @UiField(provided = true)
    PaginatorView paginatorView;

    @Inject
    public CollectionElementsListViewImpl(PaginatorPresenter paginatorPresenter) {
        this.paginatorPresenter = paginatorPresenter;
        paginatorView = this.paginatorPresenter.getView();
        elementList = new ListBox<>();
        initWidget(ourUiBinder.createAndBindUi(this));
        elementList.addSelectionHandler(event -> {
            GWT.log("[CollectionElementsListViewImpl] Selection changed");
            selectionChangedHandler.handleSelectionChanged();
        });
        elementList.setRenderer(new CollectionElementRenderer());
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
    public void setSelectionChangedHandler(@Nonnull SelectionChangedHandler handler) {
        this.selectionChangedHandler = checkNotNull(handler);
    }

    @Override
    public void setSelection(@Nonnull CollectionElementId selection) {
        GWT.log("[CollectionElementsListViewImpl] setSelection() to " + selection);
        elementList.setSelection(selection);
    }

    @Override
    public void clearSelection() {
        elementList.clearSelection();
    }

    @Nonnull
    @Override
    public Optional<CollectionElementId> getSelection() {
        return elementList.getSelection();
    }

    @Override
    public void setElements(@Nonnull List<CollectionElementId> elements) {
        GWT.log("[CollectionElementsListViewImpl] setElements");
        elementList.setListData(elements);
    }
}