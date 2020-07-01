package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.RegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.HasFormRegionPagedChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
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
    private final GridCellPresenterFactory cellPresenterFactory;

    @Nonnull
    private final FormControlStackPresenterFactory controlStackPresenterFactory;

    @Nonnull
    private final List<GridCellPresenter> cellPresenters = new ArrayList<>();

    private final Map<GridColumnId, GridCellPresenter> cellPresentersById = new HashMap<>();

    private GridControlDescriptorDto gridControlDescriptor = GridControlDescriptorDto.get(ImmutableList.of(), null);

    private Map<GridColumnId, GridCellContainer> cellContainersById = new HashMap<>();

    private Optional<FormSubjectDto> subject = Optional.empty();

    private RegionPageChangedHandler regionPageChangedHandler = () -> {};

    private boolean enabled = true;

    private Optional<GridColumnVisibilityManager> columnVisibilityManager = Optional.empty();

    @Inject
    public GridRowPresenter(@Nonnull GridRowView view,
                            GridCellPresenterFactory cellPresenterFactory, @Nonnull FormControlStackPresenterFactory controlStackPresenterFactory) {
        this.view = checkNotNull(view);
        this.cellPresenterFactory = checkNotNull(cellPresenterFactory);
        this.controlStackPresenterFactory = controlStackPresenterFactory;
    }

    public void clear() {
    }

    @Nonnull
    public GridRowData getFormDataValue() {
        ImmutableList<GridCellData> cellData =
                cellPresentersById.values().stream()
                              .map(GridCellPresenter::getValue)
                              .collect(toImmutableList());
        return GridRowData.get(subject.map(FormSubjectDto::toFormSubject).orElse(null),
                               cellData);
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

    public void setColumnDescriptors(GridControlDescriptorDto gridDescriptor) {
        if(this.gridControlDescriptor.equals(gridDescriptor)) {
            return;
        }
        this.gridControlDescriptor = checkNotNull(gridDescriptor);
        cellPresenters.clear();
        cellPresentersById.clear();
        view.clear();
        cellContainersById.clear();
        double totalSpan = gridControlDescriptor.getNestedColumnCount();
        ImmutableList<GridColumnDescriptorDto> columnDescriptors = gridDescriptor.getColumns();
        range(0, columnDescriptors.size())
                .mapToObj(columnDescriptors::get)
                .forEach(columnDescriptor -> {
                    double span = columnDescriptor.getNestedColumnCount();
                    double weight = span / totalSpan;
                    FormControlStackPresenter controlStackPresenter = controlStackPresenterFactory.create(columnDescriptor.getFormControlDescriptor(),
                                                                                                          columnDescriptor.getRepeatability(),
                                                                                                          FormRegionPosition.NESTED);

                    GridCellPresenter cellPresenter = cellPresenterFactory.create(columnDescriptor,
                                                                                  controlStackPresenter);
                    columnVisibilityManager.ifPresent(cellPresenter::setColumnVisibilityManager);
                    GridCellContainer cellContainer = view.addCell();
                    cellContainer.setWeight(weight);
                    GridColumnId columnId = columnDescriptor.getId();
                    boolean visible = isColumnVisible(columnId);
                    cellContainer.setVisible(visible);
                    cellContainersById.put(columnId, cellContainer);
                    cellPresenter.start(cellContainer);
                    cellPresenter.setRegionPageChangedHandler(regionPageChangedHandler);
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
    public void setRegionPageChangedHandler(@Nonnull RegionPageChangedHandler handler) {
        this.regionPageChangedHandler = checkNotNull(handler);
        cellPresenters.forEach(cp -> cp.setRegionPageChangedHandler(handler));
    }

    public void setValue(GridRowDataDto formDataObject) {
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
            for(GridColumnDescriptorDto descriptor : gridControlDescriptor.getColumns()) {
                GridColumnId columnId = descriptor.getId();
                GridCellContainer cellContainer = cellContainersById.get(columnId);
                if(cellContainer != null) {
                    boolean visible;
                    if(descriptor.isLeafColumnDescriptor()) {
                        visible = cvm.isVisible(descriptor.getId());
                    }
                    else {
                        visible = descriptor.getLeafColumnDescriptors()
                                            .map(GridColumnDescriptorDto::getId)
                                            .anyMatch(cvm::isVisible);
                    }

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
