package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.RegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridPresenter implements HasGridColumnVisibilityManager, HasFormRegionFilterChangedHandler {

    @Nonnull
    private final GridView view;

    @Nonnull
    private final GridHeaderPresenter headerPresenter;

    @Nonnull
    private final Provider<GridRowPresenter> rowPresenterProvider;

    @Nonnull
    private final List<GridRowPresenter> rowPresenters = new ArrayList<>();

    @Nonnull
    private final List<GridRowViewContainer> rowContainers = new ArrayList<>();

    @Nonnull
    private GridControlDescriptorDto descriptor = GridControlDescriptorDto.get(ImmutableList.of(),
                                                                         null);

    @Nonnull
    private RegionPageChangedHandler regionPageChangedHandler = () -> {
    };

    private boolean enabled = true;

    @Nonnull
    private GridColumnVisibilityManager columnVisibilityManager = new GridColumnVisibilityManager();

    private HandlerRegistration visibleColumnsChangedHandlerRegistration = () -> {
    };

    private boolean topLevel = false;

    @Nonnull
    private FormRegionOrderingChangedHandler formRegionOrderingChangedHandler = () -> {};

    @Inject
    public GridPresenter(@Nonnull GridView view,
                         @Nonnull GridHeaderPresenter headerPresenter,
                         @Nonnull Provider<GridRowPresenter> rowPresenterProvider) {
        this.view = checkNotNull(view);
        this.headerPresenter = headerPresenter;
        this.rowPresenterProvider = rowPresenterProvider;
    }

    public void clearValue() {
        view.clear();
    }

    @Nonnull
    public ImmutableList<FormRegionPageRequest> getPageRequests(@Nonnull FormSubject formSubject,
                                                                @Nonnull FormRegionId formRegionId) {
        int pageNumber = view.getPageNumber();
        PageRequest pageRequest = PageRequest.requestPageWithSize(pageNumber, FormPageRequest.DEFAULT_PAGE_SIZE);
        FormRegionPageRequest gridPageRequest = FormRegionPageRequest.get(formSubject,
                                                                          formRegionId,
                                                                          FormPageRequest.SourceType.GRID_CONTROL,
                                                                          pageRequest);

        ImmutableList<FormRegionPageRequest> rowRequests = rowPresenters
                .stream()
                .map(GridRowPresenter::getPageRequests)
                .flatMap(ImmutableList::stream)
                .collect(toImmutableList());
        return ImmutableList.<FormRegionPageRequest>builder()
                .add(gridPageRequest)
                .addAll(rowRequests)
                .build();

    }

    public GridControlData getValue() {
        ImmutableList<GridRowData> rows = rowPresenters
                .stream()
                .map(GridRowPresenter::getFormDataValue)
                .collect(toImmutableList());
        Page<GridRowData> page = new Page<>(1, 1, rows, rows.size());
        return GridControlData.get(descriptor.toFormControlDescriptor(), page, ImmutableSet.of());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        view.setEnabled(enabled);
        rowPresenters.forEach(rowPresenter -> rowPresenter.setEnabled(enabled));
        rowContainers.forEach(rowContainer -> rowContainer.setEnabled(enabled));
    }

    public void setRegionPageChangedHandler(@Nonnull RegionPageChangedHandler regionPageChangedHandler) {
        this.regionPageChangedHandler = checkNotNull(regionPageChangedHandler);
        rowPresenters.forEach(row -> row.setRegionPageChangedHandler(regionPageChangedHandler));
    }

    public void setTopLevel() {
        this.topLevel = true;
        ImmutableSet<GridColumnId> gridColumnIds = descriptor.getLeafColumns()
                                                             .map(GridColumnDescriptorDto::getId)
                                                             .collect(toImmutableSet());
        columnVisibilityManager.setVisibleColumns(gridColumnIds);
    }


    public void setValue(GridControlDataDto value) {
        rowPresenters.clear();
        view.clear();
        // List of objects
        Page<GridRowDataDto> rowsPage = value.getRows();
        rowsPage.getPageElements()
                .stream()
                .map(this::toGridRowPresenter)
                .forEach(this::addRow);
        view.setPageCount(rowsPage.getPageCount());
        view.setPageNumber(rowsPage.getPageNumber());
        view.setElementCount(rowsPage.getTotalElements());
        view.setPaginatorVisible(rowsPage.getPageCount() > 1);
        view.setEnabled(enabled);
        ImmutableSet<FormRegionOrdering> ordering = value.getOrdering();
        headerPresenter.setOrdering(ordering);
    }

    private GridRowPresenter toGridRowPresenter(GridRowDataDto rowDataValue) {
        GridRowPresenter rowPresenter = createAndSetupGridRowPresenter();
        rowPresenter.setValue(rowDataValue);
        return rowPresenter;
    }

    private GridRowPresenter createAndSetupGridRowPresenter() {
        GridRowPresenter rowPresenter = rowPresenterProvider.get();
        rowPresenter.setRegionPageChangedHandler(regionPageChangedHandler);
        rowPresenter.setColumnDescriptors(descriptor);
        rowPresenter.setEnabled(enabled);
        rowPresenter.setColumnVisibilityManager(columnVisibilityManager);
        return rowPresenter;
    }

    private void addRow(GridRowPresenter presenter) {
        GridRowViewContainer rowContainer = view.addRow();
        rowContainer.setEnabled(enabled);
        rowContainer.setDeleteHandler(() -> {
            view.removeRow(rowContainer);
            rowPresenters.remove(presenter);
        });
        presenter.start(rowContainer);
        rowPresenters.add(presenter);
        rowContainers.add(rowContainer);
    }

    public IsWidget getView() {
        return view;
    }

    public void hideHeaderRow() {
        view.hideHeader();
    }

    public void requestFocus() {
        view.requestFocus();
    }

    public void setDescriptor(GridControlDescriptorDto descriptor) {
        if (this.descriptor.equals(descriptor)) {
            return;
        }
        clear();
        this.descriptor = checkNotNull(descriptor);
        headerPresenter.setGridDescriptor(descriptor);
    }

    public void clear() {
        view.clear();
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        headerPresenter.start(view.getHeaderContainer());
        headerPresenter.addFilteredColumnsChangedHandler(source -> {
            // We only take direction from the filter menu if its for the top level grid
            // in the situation where there are nested grids
            if (topLevel) {
                columnVisibilityManager.setVisibleColumns(source.getFilteredColumns());
            }
        });
        view.setPageNumberChangedHandler(pageNumber -> regionPageChangedHandler.handleRegionPageChanged());
        view.setNewRowHandler(() -> {
            if(!enabled) {
                return;
            }
            GridRowPresenter presenter = createAndSetupGridRowPresenter();
            addRow(presenter);
            presenter.requestFocus();
        });
        headerPresenter.setGridColumnOrderingChangeHandler(this::handlerOrderingChanged);
    }

    private void handlerOrderingChanged() {
        this.formRegionOrderingChangedHandler.handleFormRegionOrderingChanged();
    }

    @Override
    public void setColumnVisibilityManager(@Nonnull GridColumnVisibilityManager columnVisibilityManager) {
        this.columnVisibilityManager = checkNotNull(columnVisibilityManager);
        // Remove any existing handler so that we don't receive multiple events
        this.visibleColumnsChangedHandlerRegistration.removeHandler();
        this.visibleColumnsChangedHandlerRegistration
                = this.columnVisibilityManager.addVisibleColumnsChangedHandler(this::updateVisibleColumns);
        this.headerPresenter.setColumnVisibilityManager(columnVisibilityManager);
        rowPresenters.forEach(row -> row.setColumnVisibilityManager(columnVisibilityManager));
        updateVisibleColumns();
    }

    public void updateVisibleColumns() {
        // Columns in header
        headerPresenter.updateVisibleColumns();
        // Show/hide columns in rows
        rowPresenters.forEach(GridRowPresenter::updateColumnVisibility);
    }

    public ImmutableList<FormRegionOrdering> getOrdering() {
        return headerPresenter.getGridColumnOrdering();
    }

    public void setOrderByChangedHandler(FormRegionOrderingChangedHandler orderByChangedHandler) {
        this.formRegionOrderingChangedHandler = checkNotNull(orderByChangedHandler);
    }

    public ImmutableSet<FormRegionFilter> getFilters() {
        return headerPresenter.getFilters();
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {
        headerPresenter.setFormRegionFilterChangedHandler(handler);
    }
}
