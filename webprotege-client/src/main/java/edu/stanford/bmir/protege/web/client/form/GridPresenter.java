package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.data.GridControlData;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowData;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridPresenter {

    @Nonnull
    private final GridView view;

    @Nonnull
    private final GridHeaderPresenter headerPresenter;

    @Nonnull
    private final Provider<GridRowPresenter> rowPresenterProvider;

    @Nonnull
    private GridControlDescriptor descriptor = GridControlDescriptor.get(ImmutableList.of(),
                                                                         null);

    @Nonnull
    private FormRegionPageChangedHandler formRegionPageChangedHandler = () -> {};

    @Inject
    public GridPresenter(@Nonnull GridView view,
                         @Nonnull GridHeaderPresenter headerPresenter,
                         @Nonnull Provider<GridRowPresenter> rowPresenterProvider) {
        this.view = checkNotNull(view);
        this.headerPresenter = headerPresenter;
        this.rowPresenterProvider = rowPresenterProvider;
        view.setPageNumberChangedHandler(pageNumber -> formRegionPageChangedHandler.handleFormRegionPageChanged());
    }

    public void clearValue() {
        view.clear();
    }

    @Nonnull
    public ImmutableList<FormRegionPageRequest> getPageRequests(@Nonnull FormSubject formSubject,
                                                                @Nonnull FormRegionId formRegionId) {
        int pageNumber = view.getPageNumber();
        int pageSize = view.getPageSize();
        PageRequest pageRequest = PageRequest.requestPageWithSize(pageNumber, pageSize);
        FormRegionPageRequest gridPageRequest = FormRegionPageRequest.get(formSubject, formRegionId, FormPageRequest.SourceType.GRID_CONTROL, pageRequest);

        ImmutableList<FormRegionPageRequest> rowRequests = view.getRows()
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
        ImmutableList<GridRowData> rows = view.getRows()
                                              .stream()
                                              .map(GridRowPresenter::getFormDataValue)
                                              .collect(toImmutableList());
        Page<GridRowData> page = new Page<>(1, 1, rows, rows.size());
        return GridControlData.get(descriptor, page);
    }

    public void setFormRegionPageChangedHandler(@Nonnull FormRegionPageChangedHandler formRegionPageChangedHandler) {
        this.formRegionPageChangedHandler = checkNotNull(formRegionPageChangedHandler);
        view.getRows().forEach(row -> row.setFormRegionPageChangedHandler(formRegionPageChangedHandler));
    }


    public void setValue(GridControlData value) {
        GWT.log("[GridPresenter] (setValue) ");
        clear();
        // List of objects
        Page<GridRowData> rowsPage = value.getRows();
        List<GridRowPresenter> rows = rowsPage
                                           .getPageElements()
                                           .stream()
                                           .map(rowDataValue -> {
                                               GridRowPresenter rowPresenter = rowPresenterProvider.get();
                                               rowPresenter.setFormRegionPageChangedHandler(formRegionPageChangedHandler);
                                               rowPresenter.setColumnDescriptors(descriptor.getColumns());
                                               rowPresenter.setValue(rowDataValue);
                                               return rowPresenter;
                                           })
                                           .collect(Collectors.toList());
        view.setRows(rows);
        view.setPageCount(rowsPage.getPageCount());
        view.setPageNumber(rowsPage.getPageNumber());
        view.setPaginatorVisible(rowsPage.getPageCount() > 1);
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

    public void setDescriptor(GridControlDescriptor descriptor) {
        if(this.descriptor.equals(descriptor)) {
            return;
        }
        clear();
        this.descriptor = checkNotNull(descriptor);
        headerPresenter.setColumns(descriptor.getColumns());
        view.setNewRowHandler(() -> {
            GridRowPresenter rowPresenter = rowPresenterProvider.get();
            rowPresenter.setColumnDescriptors(descriptor.getColumns());
            return rowPresenter;
        });
    }

    public void clear() {
        view.clear();
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        headerPresenter.start(view.getHeaderContainer());
    }


}
