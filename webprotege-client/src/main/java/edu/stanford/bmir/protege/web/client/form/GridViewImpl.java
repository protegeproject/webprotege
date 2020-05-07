package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import dagger.multibindings.IntoSet;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridViewImpl extends Composite implements GridView {

    private final PaginatorPresenter paginatorPresenter;

    private Optional<NewRowHandler> newRowHandler = Optional.empty();

    private boolean enabled = true;

    interface GridViewImplUiBinder extends UiBinder<HTMLPanel, GridViewImpl> {

    }

    private static GridViewImplUiBinder ourUiBinder = GWT.create(GridViewImplUiBinder.class);

    @UiField
    SimplePanel headerContainer;

    @UiField(provided = true)
    ValueListEditor<GridRowPresenter> rowEditor;

    @UiField(provided = true)
    PaginatorView paginatorView;

    @Inject
    public GridViewImpl(PaginatorPresenter paginatorPresenter) {
        this.paginatorView = paginatorPresenter.getView();
        this.paginatorPresenter = paginatorPresenter;
        rowEditor = new ValueListFlexEditorImpl<>(() -> {
            GridRowPresenterAdapter gridRowPresenterAdapter = new GridRowPresenterAdapter();
            newRowHandler.ifPresent(handler -> {
                GridRowPresenter presenter = handler.createRow();
                presenter.setEnabled(enabled);
                gridRowPresenterAdapter.setValue(presenter);
            });
            return gridRowPresenterAdapter;
        });
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        rowEditor.setEnabled(enabled);
        getRows()
            .forEach(rowPresenter -> rowPresenter.setEnabled(enabled));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getHeaderContainer() {
        return headerContainer;
    }

    @Override
    public void requestFocus() {
        rowEditor.requestFocus();
    }

    @Override
    public void setRows(@Nonnull List<GridRowPresenter> rowPresenters) {
        rowEditor.setValue(rowPresenters);
    }

    @Nonnull
    @Override
    public List<GridRowPresenter> getRows() {
        return rowEditor.getValue().orElse(ImmutableList.of());
    }

    @Override
    public void setNewRowHandler(@Nonnull NewRowHandler newRowHandler) {
        this.newRowHandler = Optional.of(checkNotNull(newRowHandler));
    }

    @Override
    public void setElementCount(long elementCount) {
        paginatorPresenter.setElementCount(elementCount);
    }

    @Override
    public int getPageSize() {
        return paginatorPresenter.getPageSize();
    }

    @Override
    public void clear() {
        rowEditor.clearValue();
        paginatorPresenter.setPageNumber(1);
        paginatorPresenter.setPageCount(1);
        paginatorView.setVisible(false);
    }

    @Override
    public void hideHeader() {
        headerContainer.getElement().getStyle().setDisplay(Style.Display.NONE);
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

    @Override
    public void setPaginatorVisible(boolean visible) {
        paginatorView.setVisible(visible);
    }
}
