package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

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
 * 2019-11-27
 */
public class GridHeaderPresenter {

    interface GridColumnVisibilityChangedHandler {
        void handleColumnVisibilityChanged();
    }

    @Nonnull
    private final GridHeaderView view;

    @Nonnull
    private final Provider<GridHeaderColumnPresenter> headerColumnPresenterProvider;

    @Nonnull
    private final List<GridHeaderColumnPresenter> columnPresenters = new ArrayList<>();

    @Nonnull
    private final LanguageMapCurrentLocaleMapper localeMapper;

    @Inject
    public GridHeaderPresenter(@Nonnull GridHeaderView view,
                               @Nonnull Provider<GridHeaderColumnPresenter> headerColumnPresenterProvider,
                               @Nonnull LanguageMapCurrentLocaleMapper localeMapper) {
        this.view = checkNotNull(view);
        this.headerColumnPresenterProvider = checkNotNull(headerColumnPresenterProvider);
        this.localeMapper = checkNotNull(localeMapper);
    }

    void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void clear() {
        view.clear();
        columnPresenters.clear();;
    }

    public void setColumns(@Nonnull ImmutableList<GridColumnDescriptor> columnDescriptors) {
        clear();
        double totalSpan = columnDescriptors.stream()
                         .map(GridColumnDescriptor::getNestedColumnCount)
                         .reduce((left, right) -> left + right)
                         .orElse(0);
        columnDescriptors.forEach(columnDescriptor -> {
            double span = columnDescriptor.getNestedColumnCount();
            double weight = span / totalSpan;
            GridHeaderColumnPresenter columnPresenter = headerColumnPresenterProvider.get();
            columnPresenters.add(columnPresenter);
            columnPresenter.setColumnDescriptor(columnDescriptor);
            IsWidget headerColumnView = columnPresenter.getView();
            view.addColumnHeader(headerColumnView, weight);
        });
        getLeafColumns().forEach(dc -> view.addColumnToFilterList(
                localeMapper.getValueForCurrentLocale(dc.getLabel()),
                dc.getId()
        ));
    }


    public ImmutableList<GridColumnId> getVisibleColumns() {
        return view.getVisibleColumns();
    }

    public ImmutableList<GridColumnDescriptor> getLeafColumns() {
        return columnPresenters.stream()
                .flatMap(columnPresenter -> columnPresenter.getLeafColumns().stream())
                .collect(toImmutableList());

    }

    public void setColumnVisibilityChangedHandler(GridColumnVisibilityChangedHandler handler) {
        view.setGridColumnVisibilityChangedHandler(() -> {
            ImmutableList<GridColumnId> visibleColumns = view.getVisibleColumns();
            setVisibleColumns(visibleColumns);
            handler.handleColumnVisibilityChanged();
        });
    }

    public void setVisibleColumns(ImmutableList<GridColumnId> visibleColumns) {
        for(int i = 0; i < columnPresenters.size(); i++) {
            GridHeaderColumnPresenter columnPresenter = columnPresenters.get(i);
            boolean visible = columnPresenter.getColumnId().map(visibleColumns::contains).orElse(false);
            view.setColumnVisible(i, visible);
        }
    }

}
