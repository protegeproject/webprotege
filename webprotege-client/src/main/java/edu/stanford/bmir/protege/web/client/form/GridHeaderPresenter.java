package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.form.data.FormRegionFilter;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderPresenter implements HasGridColumnFilter, HasGridColumnVisibilityManager, HasGridColumnOrdering, HasFormRegionFilterChangedHandler {

    @Nonnull
    private final GridHeaderView view;

    @Nonnull
    private final Provider<GridHeaderColumnPresenter> headerColumnPresenterProvider;

    @Nonnull
    private final Map<GridColumnId, GridHeaderColumnPresenter> columnPresenters = new LinkedHashMap<>();

    @Nonnull
    private final LanguageMapCurrentLocaleMapper localeMapper;

    private Optional<GridColumnVisibilityManager> columnVisibilityManager = Optional.empty();

    private Map<GridColumnDescriptorDto, GridHeaderCellContainer> containersByDescriptor = new HashMap<>();

    @Nonnull
    private ChangeHandler orderByChangedHandler = () -> {};

    private Optional<FormRegionOrdering> orderBy = Optional.empty();

    @Nonnull
    private final GridFilterPresenter filterPresenter;

    @Nonnull
    private List<FormRegionFilter> filters = new ArrayList<>();

    @Nonnull
    private FormRegionFilterChangedHandler formRegionFilterChangedHandler = event -> {};

    @Inject
    public GridHeaderPresenter(@Nonnull GridHeaderView view,
                               @Nonnull Provider<GridHeaderColumnPresenter> headerColumnPresenterProvider,
                               @Nonnull LanguageMapCurrentLocaleMapper localeMapper,
                               @Nonnull GridFilterPresenter filterPresenter) {
        this.view = checkNotNull(view);
        this.headerColumnPresenterProvider = checkNotNull(headerColumnPresenterProvider);
        this.localeMapper = checkNotNull(localeMapper);
        this.filterPresenter = checkNotNull(filterPresenter);
    }

    @Override
    public void setColumnVisibilityManager(@Nonnull GridColumnVisibilityManager columnVisibilityManager) {
        this.columnVisibilityManager = Optional.of(checkNotNull(columnVisibilityManager));
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setEditGridFilterHandler(this::handleEditGridFilter);
    }

    private void handleEditGridFilter() {
        ImmutableList<GridColumnDescriptorDto> columnDescriptors = containersByDescriptor.keySet()
                              .stream()
                              .collect(toImmutableList());
        filterPresenter.showModal(columnDescriptors,
                                  ImmutableList.copyOf(filters),
                                  this::handleApplyFilters, () -> {});
    }

    private void handleApplyFilters(ImmutableList<FormRegionFilter> filters) {
        this.filters.clear();
        this.filters.addAll(filters);
        updateFilterActiveDisplay();
        formRegionFilterChangedHandler.handleFormRegionFilterChanged(new FormRegionFilterChangedEvent());
    }

    private void updateFilterActiveDisplay() {
        boolean filterActive = !this.filters.isEmpty();
        view.setFilterActive(filterActive);
    }

    public void clear() {
        view.clear();
        containersByDescriptor.clear();
        columnPresenters.clear();
    }

    public void setGridDescriptor(@Nonnull GridControlDescriptorDto descriptor) {
        clear();
        double totalSpan = descriptor.getNestedColumnCount();
        descriptor.getLeafColumns().forEach(leafColumnDescriptor -> {
            double span = leafColumnDescriptor.getNestedColumnCount();
            double weight = span / totalSpan;
            GridHeaderColumnPresenter columnPresenter = headerColumnPresenterProvider.get();
            columnPresenter.setColumnHeaderClickedHandler(() -> {
                this.handleColumnClicked(leafColumnDescriptor.getId());
            });
            columnPresenters.put(leafColumnDescriptor.getId(), columnPresenter);
            columnPresenter.setColumnDescriptor(leafColumnDescriptor);
            GridHeaderCellContainer columnHeaderContainer = view.addColumnHeader();
            columnHeaderContainer.setWeight(weight);
            columnPresenter.start(columnHeaderContainer);
            containersByDescriptor.put(leafColumnDescriptor, columnHeaderContainer);
            String label = localeMapper.getValueForCurrentLocale(leafColumnDescriptor.getLabel());
            view.addColumnToFilterList(label, leafColumnDescriptor.getId());
        });
    }

    private void handleColumnClicked(GridColumnId id) {
        this.orderBy = Optional.empty();
        columnPresenters.values().forEach(cp -> {
            if(cp.isPresenterFor(id)) {
                FormRegionOrderingDirection dir = cp.toggleSortOrder();
                this.orderBy = Optional.of(FormRegionOrdering.get(id, dir));
            }
            else {
                cp.clearSortOrder();
            }
        });
        orderByChangedHandler.handleGridColumnOrderingChanged();
    }

    @Override
    public HandlerRegistration addFilteredColumnsChangedHandler(@Nonnull FilteredColumnsChangedHandler handler) {
        return view.addFilteredColumnsChangedHandler(handler);
    }

    @Override
    public void addColumnToFilterList(@Nonnull String columnName, @Nonnull GridColumnId columnId) {
        view.addColumnToFilterList(columnName, columnId);
    }

    @Override
    public ImmutableSet<GridColumnId> getFilteredColumns() {
        return view.getFilteredColumns();
    }

    public void updateVisibleColumns() {
        ImmutableSet<GridColumnId> visibleColumns = columnVisibilityManager.map(GridColumnVisibilityManager::getVisibleColumns)
                                                                           .orElse(ImmutableSet.of());
        containersByDescriptor.forEach((columnDescriptor, container) -> {
            boolean visible = !columnDescriptor.isLeafColumnDescriptor()
                        || visibleColumns.contains(columnDescriptor.getId());
            container.setVisible(visible);
        });
    }

    @Override
    public void setGridColumnOrderingChangeHandler(@Nonnull ChangeHandler handler) {
        this.orderByChangedHandler = checkNotNull(handler);
    }

    @Nonnull
    @Override
    public ImmutableList<FormRegionOrdering> getGridColumnOrdering() {
        return orderBy.map(ImmutableList::of).orElse(ImmutableList.of());
    }

    public void setOrdering(@Nonnull ImmutableSet<FormRegionOrdering> ordering) {
        if(ordering.isEmpty()) {
            this.orderBy = Optional.empty();
            columnPresenters.values()
                            .forEach(GridHeaderColumnPresenter::clearSortOrder);
        }
        else {
            Map<GridColumnId, FormRegionOrdering> orderingsById = new HashMap<>();
            ordering.stream()
                    .filter(o -> o.getRegionId() instanceof GridColumnId)
                    .forEach(o -> orderingsById.put((GridColumnId) o.getRegionId(), o));
            this.orderBy = Optional.empty();
            columnPresenters.forEach((columnId, columnPresenter) -> {
                FormRegionOrdering columnOrdering = orderingsById.get(columnId);
                if(!orderBy.isPresent()) {
                    orderBy = Optional.ofNullable(columnOrdering);
                }
                if(columnOrdering != null) {
                    columnPresenter.setSortOrder(columnOrdering.getDirection());
                }
                else {
                    columnPresenter.clearSortOrder();
                }
            });
        }
    }

    @Nonnull
    public ImmutableSet<FormRegionFilter> getFilters() {
        return ImmutableSet.copyOf(filters);
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {
        this.formRegionFilterChangedHandler = checkNotNull(handler);
    }
}
