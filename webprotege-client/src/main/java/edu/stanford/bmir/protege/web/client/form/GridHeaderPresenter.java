package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.*;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderPresenter implements HasGridColumnFilter, HasGridColumnVisibilityManager {

    @Nonnull
    private final GridHeaderView view;

    @Nonnull
    private final Provider<GridHeaderColumnPresenter> headerColumnPresenterProvider;

    @Nonnull
    private final Map<GridColumnId, GridHeaderColumnPresenter> columnPresenters = new LinkedHashMap<>();

    @Nonnull
    private final LanguageMapCurrentLocaleMapper localeMapper;

    private Optional<GridColumnVisibilityManager> columnVisibilityManager = Optional.empty();

    private Map<GridColumnDescriptor, GridColumnHeaderContainer> containersByDescriptor = new HashMap<>();

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
            columnPresenters.put(leafColumnDescriptor.getId(), columnPresenter);
            columnPresenter.setColumnDescriptor(leafColumnDescriptor);
            GridColumnHeaderContainer columnHeaderContainer = view.addColumnHeader();
            columnHeaderContainer.setWeight(weight);
            columnPresenter.start(columnHeaderContainer);
            containersByDescriptor.put(leafColumnDescriptor, columnHeaderContainer);
            String label = localeMapper.getValueForCurrentLocale(leafColumnDescriptor.getLabel());
            view.addColumnToFilterList(label, leafColumnDescriptor.getId());
        });
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
}
