package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.data.GridControlData;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowData;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.List;
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
    private GridControlDescriptor descriptor = GridControlDescriptor.get(ImmutableList.of());

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

    public IsWidget getView() {
        return view;
    }

    public void requestFocus() {
        view.requestFocus();
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        headerPresenter.start(view.getHeaderContainer());
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

    public void setValue(GridControlData value) {
        clear();
        // List of objects
        List<GridRowPresenter> rows = value.getRows()
             .stream()
             .map(rowDataValue -> {
                     GridRowPresenter rowPresenter = rowPresenterProvider.get();
                     rowPresenter.setColumnDescriptors(descriptor.getColumns());
                     rowPresenter.setValue(rowDataValue);
                     return rowPresenter;
             })
             .collect(Collectors.toList());
        view.setRows(rows);
    }

    public GridControlData getValue() {
        ImmutableList<GridRowData> rows = view.getRows().stream()
                                                    .map(GridRowPresenter::getFormDataValue)
                                                    .collect(toImmutableList());
        return GridControlData.get(descriptor, rows);
    }


}
