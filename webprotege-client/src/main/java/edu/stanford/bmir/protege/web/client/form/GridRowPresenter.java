package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.HasFormRegionPagedChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.data.GridCellData;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowData;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.IntStream.range;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridRowPresenter implements HasFormRegionPagedChangedHandler, HasGridColumnVisibilityManager {

    @Nonnull
    private final GridRowView view;

    @Nonnull
    private final Provider<GridCellPresenter> cellPresenterProvider;

    @Nonnull
    private final List<GridCellPresenter> cellPresenters = new ArrayList<>();

    private final Map<GridColumnId, GridCellPresenter> cellPresentersById = new HashMap<>();

    private GridControlDescriptor gridControlDescriptor = GridControlDescriptor.get(ImmutableList.of(), null);

    private Map<GridColumnId, GridCellContainer> cellContainersById = new HashMap<>();

    private Optional<FormSubject> subject = Optional.empty();

    private FormRegionPageChangedHandler formRegionPageChangedHandler = () -> {};

    private boolean enabled = true;

    private Optional<GridColumnVisibilityManager> columnVisibilityManager = Optional.empty();

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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        cellPresenters.forEach(cellPresenter -> cellPresenter.setEnabled(enabled));
    }

    public void requestFocus() {
        cellPresenters.stream()
                      .findFirst()
                      .ifPresent(GridCellPresenter::requestFocus);
    }

    public void setColumnDescriptors(GridControlDescriptor gridDescriptor) {
        if(this.gridControlDescriptor.equals(gridDescriptor)) {
            return;
        }
        this.gridControlDescriptor = checkNotNull(gridDescriptor);
        cellPresenters.clear();
        cellPresentersById.clear();
        view.clear();
        cellContainersById.clear();
        double totalSpan = gridControlDescriptor.getNestedColumnCount();
        ImmutableList<GridColumnDescriptor> columnDescriptors = gridDescriptor.getColumns();
        range(0, columnDescriptors.size())
                .mapToObj(columnDescriptors::get)
                .forEach(columnDescriptor -> {
                    double span = columnDescriptor.getNestedColumnCount();
                    double weight = span / totalSpan;
                    GridCellPresenter cellPresenter = cellPresenterProvider.get();
                    columnVisibilityManager.ifPresent(cellPresenter::setColumnVisibilityManager);
                    GridCellContainer cellContainer = view.addCell();
                    cellContainer.setWeight(weight);
                    GridColumnId columnId = columnDescriptor.getId();
                    boolean visible = isColumnVisible(columnId);
                    cellContainer.setVisible(visible);
                    cellContainersById.put(columnId, cellContainer);
                    cellPresenter.start(cellContainer);
                    cellPresenter.setDescriptor(columnDescriptor);
                    cellPresenter.setFormRegionPageChangedHandler(formRegionPageChangedHandler);
                    cellPresenter.setEnabled(enabled);
                    cellPresenters.add(cellPresenter);
                    cellPresentersById.put(columnId, cellPresenter);
                });
    }

    @Override
    public void setColumnVisibilityManager(@Nonnull GridColumnVisibilityManager columnVisibilityManager) {
        this.columnVisibilityManager = Optional.of(checkNotNull(columnVisibilityManager));
        cellPresenters.forEach(presenter -> presenter.setColumnVisibilityManager(columnVisibilityManager));
        updateColumnVisibility();
    }

    @Override
    public void setFormRegionPageChangedHandler(@Nonnull FormRegionPageChangedHandler handler) {
        this.formRegionPageChangedHandler = checkNotNull(handler);
        cellPresenters.forEach(cp -> cp.setFormRegionPageChangedHandler(handler));
    }

    public void setValue(GridRowData formDataObject) {
        this.subject = formDataObject.getSubject();
        cellPresenters.forEach(GridCellPresenter::clear);
        formDataObject.getCells()
                      .forEach(cellData -> {
                          GridCellPresenter cellPresenter = cellPresentersById.get(cellData.getColumnId());
                          if(cellPresenter != null) {
                              cellPresenter.setValue(cellData);
                          }
                      });
        updateColumnVisibility();
    }

    public void updateColumnVisibility() {
        columnVisibilityManager.ifPresent(cvm -> {
            double totalSpan = gridControlDescriptor.getNestedColumnCount();
            for(GridColumnDescriptor descriptor : gridControlDescriptor.getColumns()) {
                GridColumnId columnId = descriptor.getId();
                boolean visible = !descriptor.isLeafColumnDescriptor() || cvm.isVisible(columnId);
                GridCellContainer cellContainer = cellContainersById.get(columnId);
                if(cellContainer != null) {
                    cellContainer.setVisible(visible);
                    long nestedLeafCount = descriptor.getLeafColumnDescriptors()
                                                     .filter(nestedLeafColumn -> cvm.isVisible(nestedLeafColumn.getId()))
                                                     .count();
                    double weight = nestedLeafCount / totalSpan;
                    cellContainer.setWeight(weight);
                }
            }
        });
        cellPresenters.forEach(GridCellPresenter::updateColumnVisibility);
    }


    private boolean isColumnVisible(@Nonnull GridColumnId columnId) {
        return columnVisibilityManager.map(cvm -> cvm.isVisible(columnId)).orElse(true);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }
}
