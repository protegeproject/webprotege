package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.data.GridCellData;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowData;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridRowPresenter {

    @Nonnull
    private final GridRowView view;

    @Nonnull
    private final Provider<GridCellPresenter> cellPresenterProvider;

    @Nonnull
    private final List<GridCellPresenter> cellPresenters = new ArrayList<>();

    private final Map<GridColumnId, GridCellPresenter> cellPresentersById = new HashMap<>();

    private ImmutableList<GridColumnDescriptor> columnDescriptors = ImmutableList.of();

    @Inject
    public GridRowPresenter(@Nonnull GridRowView view,
                            Provider<GridCellPresenter> cellPresenterProvider) {
        this.view = checkNotNull(view);
        this.cellPresenterProvider = checkNotNull(cellPresenterProvider);
    }

    @Nonnull
    public GridRowData getFormDataValue() {
        ImmutableList<GridCellData> cellData =
                cellPresenters.stream()
                              .filter(GridCellPresenter::isPresent)
                              .map(GridCellPresenter::getValue)
                              .collect(toImmutableList());
        return GridRowData.get(cellData);
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
        this.columnDescriptors.forEach(column -> {
            GridCellPresenter cellPresenter = cellPresenterProvider.get();
            AcceptsOneWidget cellContainer = view.addCell();
            cellPresenter.start(cellContainer);
            cellPresenter.setDescriptor(column);
            cellPresenters.add(cellPresenter);
            cellPresentersById.put(column.getId(), cellPresenter);
        });

    }

    public void setValue(GridRowData formDataObject) {
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
