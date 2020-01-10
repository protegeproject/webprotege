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
    private final List<GridRowPresenter> rowPresenters = new ArrayList<>();

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
        rowPresenters.clear();
    }

    public IsWidget getView() {
        return view;
    }

    public void requestFocus() {
        rowPresenters.stream()
                     .findFirst()
                     .ifPresent(GridRowPresenter::requestFocus);
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
    }

    public void clear() {
        rowPresenters.clear();
        view.clear();
    }

    public void setValue(GridControlData value) {
        clear();
        // List of objects
        value.getRows()
             .forEach(rowDataValue -> {
                     GridRowPresenter rowPresenter = rowPresenterProvider.get();
                     AcceptsOneWidget rowContainer = view.addRow();
                     rowPresenter.start(rowContainer);
                     rowPresenter.setColumnDescriptors(descriptor.getColumns());
                     rowPresenter.setValue(rowDataValue);
             });
    }

    public GridControlData getValue() {
        ImmutableList<GridRowData> rows = rowPresenters.stream()
                                                    .map(GridRowPresenter::getFormDataValue)
                                                    .collect(toImmutableList());
        return GridControlData.get(descriptor, rows);
    }


}
