package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.core.client.GWT;
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

    @Nonnull
    private AddElementHandler addElementHandler = () -> {};

    @Nonnull
    private DeleteElementHandler deleteElementHandler = (elementId) -> {};

    private SelectionChangedHandler selectionChangedHandler = () -> {};

    interface CollectionElementsListViewImplUiBinder extends UiBinder<HTMLPanel, CollectionElementsListViewImpl> {

    }

    private static CollectionElementsListViewImplUiBinder ourUiBinder = GWT.create(
            CollectionElementsListViewImplUiBinder.class);

    private SingleSelectionModel<CollectionElementId> selectionModel = new SingleSelectionModel<>(CollectionElementId::getId);

    private ListDataProvider<CollectionElementId> listDataProvider = new ListDataProvider<>();

    @UiField(provided = true)
    CellList<CollectionElementId> elementList;

    @UiField(provided = true)
    PaginatorView paginatorView;

    @Inject
    public CollectionElementsListViewImpl(PaginatorPresenter paginatorPresenter) {
        this.paginatorPresenter = paginatorPresenter;
        paginatorView = this.paginatorPresenter.getView();
        elementList = new CellList<>(new CollectionElementIdRenderer(),
                                     WebProtegeCellListResources.INSTANCE);
        elementList.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.BOUND_TO_SELECTION);
        elementList.setSelectionModel(selectionModel);
        listDataProvider.addDataDisplay(elementList);
        initWidget(ourUiBinder.createAndBindUi(this));
        selectionModel.addSelectionChangeHandler(event -> selectionChangedHandler.handleSelectionChanged());

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
        selectionModel.clear();
        selectionModel.setSelected(selection, true);
    }

    @Override
    public void clearSelection() {
        selectionModel.clear();
    }

    @Nonnull
    @Override
    public Optional<CollectionElementId> getSelection() {
        return Optional.ofNullable(selectionModel.getSelectedObject());
    }

    @Override
    public void setAddElementHandler(@Nonnull AddElementHandler addElementHandler) {
        this.addElementHandler = checkNotNull(addElementHandler);
    }

    @Override
    public void setDeleteElementHandler(@Nonnull DeleteElementHandler deleteElementHandler) {
        this.deleteElementHandler = checkNotNull(deleteElementHandler);
    }

    @Override
    public void setElements(@Nonnull List<CollectionElementId> elements) {
        elementList.setRowCount(elements.size());
        elementList.setPageSize(elements.size());
        elementList.setRowData(elements);
    }
}