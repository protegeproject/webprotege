package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.HasFormRegionPagedChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.data.GridCellData;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowData;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridRowPresenter implements HasFormRegionPagedChangedHandler {

    @Nonnull
    private final GridRowView view;

    @Nonnull
    private final Provider<GridCellPresenter> cellPresenterProvider;

    @Nonnull
    private final List<GridCellPresenter> cellPresenters = new ArrayList<>();

    private final Map<GridColumnId, GridCellPresenter> cellPresentersById = new HashMap<>();

    private ImmutableList<GridColumnDescriptor> columnDescriptors = ImmutableList.of();

    private Optional<FormSubject> subject = Optional.empty();

    private FormRegionPageChangedHandler formRegionPageChangedHandler = () -> {
    };

    private boolean enabled = true;

    @Inject
    public GridRowPresenter(@Nonnull GridRowView view,
                            Provider<GridCellPresenter> cellPresenterProvider) {
        this.view = checkNotNull(view);
        this.cellPresenterProvider = checkNotNull(cellPresenterProvider);
    }

    public void clear() {

    }

    @Nonnull
    public GridRowData getFormDataValue() {
        ImmutableList<GridCellData> cellData =
                cellPresenters.stream()
                              .filter(GridCellPresenter::isPresent)
                              .map(GridCellPresenter::getValue)
                              .collect(toImmutableList());
        return GridRowData.get(subject.orElse(null), cellData);
    }

    public ImmutableList<FormRegionPageRequest> getPageRequests() {
        return subject.map(s -> cellPresenters.stream()
                                              .map(GridCellPresenter::getPageRequest)
                                              .flatMap(ImmutableList::stream)
                                              .collect(toImmutableList()))
                      .orElse(ImmutableList.of());
    }

    public boolean isDirty() {
        return false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void requestFocus() {
        cellPresenters.stream()
                      .findFirst()
                      .ifPresent(GridCellPresenter::requestFocus);
    }

    public void setColumnDescriptors(ImmutableList<GridColumnDescriptor> columnDescriptors) {
        if(this.columnDescriptors.equals(columnDescriptors)) {
            return;
        }
        cellPresenters.clear();
        cellPresentersById.clear();
        view.clear();
        this.columnDescriptors = checkNotNull(columnDescriptors);
        double totalSpan = columnDescriptors.stream()
                                         .map(GridColumnDescriptor::getNestedColumnCount)
                                         .reduce((left, right) -> left + right)
                                         .orElse(0);
        this.columnDescriptors.forEach(column -> {
            double span = column.getNestedColumnCount();
            double weight = span / totalSpan;
            GridCellPresenter cellPresenter = cellPresenterProvider.get();
            AcceptsOneWidget cellContainer = view.addCell(weight);
            cellPresenter.start(cellContainer);
            cellPresenter.setDescriptor(column);
            cellPresenter.setFormRegionPageChangedHandler(formRegionPageChangedHandler);
            cellPresenter.setEnabled(enabled);
            cellPresenters.add(cellPresenter);
            cellPresentersById.put(column.getId(), cellPresenter);
        });

    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        cellPresenters.forEach(cellPresenter -> cellPresenter.setEnabled(enabled));
    }

    @Override
    public void setFormRegionPageChangedHandler(@Nonnull FormRegionPageChangedHandler handler) {
        this.formRegionPageChangedHandler = checkNotNull(handler);
        cellPresenters.forEach(cp -> cp.setFormRegionPageChangedHandler(handler));
    }

    public void setValue(GridRowData formDataObject) {
        this.subject = formDataObject.getSubject();
        GWT.log("[GridRowPresenter] (setValue) " + subject);
        cellPresenters.forEach(GridCellPresenter::clear);
        formDataObject.getCells()
                      .forEach(cellData -> {
                          GridCellPresenter cellPresenter = cellPresentersById.get(cellData.getColumnId());
                          if(cellPresenter != null) {
                              cellPresenter.setValue(cellData);
                          }
                      });
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }
}
