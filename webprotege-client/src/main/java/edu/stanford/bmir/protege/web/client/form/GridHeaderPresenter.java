package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderPresenter implements HasGridColumnFilter, HasGridColumnVisibilityManager, HasGridColumnOrderBy {

    @Nonnull
    private final GridHeaderView view;

    @Nonnull
    private final Provider<GridHeaderColumnPresenter> headerColumnPresenterProvider;

    @Nonnull
    private final Map<GridColumnId, GridHeaderColumnPresenter> columnPresenters = new LinkedHashMap<>();

    @Nonnull
    private final LanguageMapCurrentLocaleMapper localeMapper;

    private Optional<GridColumnVisibilityManager> columnVisibilityManager = Optional.empty();

    private Map<GridColumnDescriptor, GridHeaderCellContainer> containersByDescriptor = new HashMap<>();

    @Nonnull
    private ChangeHandler orderByChangedHandler = () -> {};

    private Optional<GridControlOrderBy> orderBy = Optional.empty();

    @Inject
    public GridHeaderPresenter(@Nonnull GridHeaderView view,
                               @Nonnull Provider<GridHeaderColumnPresenter> headerColumnPresenterProvider,
                               @Nonnull LanguageMapCurrentLocaleMapper localeMapper) {
        this.view = checkNotNull(view);
        this.headerColumnPresenterProvider = checkNotNull(headerColumnPresenterProvider);
        this.localeMapper = checkNotNull(localeMapper);
    }

    @Override
    public void setColumnVisibilityManager(@Nonnull GridColumnVisibilityManager columnVisibilityManager) {
        this.columnVisibilityManager = Optional.of(checkNotNull(columnVisibilityManager));
    }

    void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void clear() {
        view.clear();
        containersByDescriptor.clear();
        columnPresenters.clear();;
    }

    public void setGridDescriptor(@Nonnull GridControlDescriptor descriptor) {
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
                GridControlOrderByDirection dir = cp.toggleSortOrder();
                this.orderBy = Optional.of(GridControlOrderBy.get(id, dir));
            }
            else {
                cp.clearSortOrder();
            }
        });
        orderByChangedHandler.handleGridColumnOrderByChanged();
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
    public void setGridColumnOrderByChangeHandler(@Nonnull ChangeHandler handler) {
        this.orderByChangedHandler = checkNotNull(handler);
    }

    @Nonnull
    @Override
    public ImmutableList<GridControlOrderBy> getOrderBy() {
        return orderBy.map(ImmutableList::of).orElse(ImmutableList.of());
    }

    public void setOrderBy(@Nonnull ImmutableList<GridControlOrderBy> ordering) {
        if(ordering.isEmpty()) {
            this.orderBy = Optional.empty();
            columnPresenters.values()
                            .forEach(GridHeaderColumnPresenter::clearSortOrder);
        }
        else {
            GridControlOrderBy orderBy = ordering.get(0);
            this.orderBy = Optional.of(orderBy);
            columnPresenters.values().forEach(cp -> {
                if(cp.isPresenterFor(orderBy.getColumnId())) {
                    cp.setSortOrder(orderBy.getDirection());
                }
                else {
                    cp.clearSortOrder();
                }
            });
        }
    }
}
