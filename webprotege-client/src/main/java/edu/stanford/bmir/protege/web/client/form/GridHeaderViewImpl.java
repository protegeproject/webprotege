package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.filter.FilterViewPopup;
import edu.stanford.bmir.protege.web.client.library.popupmenu.MenuButton;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderViewImpl extends Composite implements GridHeaderView {

    private Map<FilterId, GridColumnId> filterMap = new HashMap<>();

    @Nonnull
    private EditGridFilterHandler editGridFilterHandler = () -> {};

    interface GridHeaderViewImplUiBinder extends UiBinder<HTMLPanel, GridHeaderViewImpl> {

    }

    private static GridHeaderViewImplUiBinder ourUiBinder = GWT.create(GridHeaderViewImplUiBinder.class);

    @UiField
    HTMLPanel headerContainer;

    @UiField
    MenuButton menuButton;

    @UiField
    Button filterButton;

    private FilterView filterView;

    private FilterViewPopup filterViewPopup = new FilterViewPopup();


    @Inject
    public GridHeaderViewImpl(FilterView filterView) {
        this.filterView = checkNotNull(filterView);
        initWidget(ourUiBinder.createAndBindUi(this));
        menuButton.addClickHandler(this::handleMenuButtonClicked);
        filterButton.addClickHandler(this::handleFilterButtonClicked);
    }

    private void handleFilterButtonClicked(ClickEvent event) {
        editGridFilterHandler.handleEditGridFilter();
    }

    private void handleMenuButtonClicked(@Nonnull ClickEvent event) {
        filterViewPopup.showFilterView(filterView,
                                       menuButton,
                                       filterSet -> {});
    }

    @Nonnull
    @Override
    public GridHeaderCellContainer addColumnHeader() {
        GridHeaderCellContainerImpl container = new GridHeaderCellContainerImpl();
        headerContainer.add(container);
        return container;
    }

    @Override
    public void clear() {
        headerContainer.clear();
        filterMap.clear();
    }

    @Override
    public void setEditGridFilterHandler(@Nonnull EditGridFilterHandler handler) {
        this.editGridFilterHandler = checkNotNull(handler);
    }

    @Override
    public HandlerRegistration addFilteredColumnsChangedHandler(@Nonnull FilteredColumnsChangedHandler handler) {
        ValueChangeHandler<FilterSet> valueChangeHandler = event -> handler.handleFilteredColumnsChanged(GridHeaderViewImpl.this);
        return filterView.addValueChangeHandler(valueChangeHandler);
    }

    @Override
    public ImmutableSet<GridColumnId> getFilteredColumns() {
        return filterView.getFilterSet()
                         .getOnFilters()
                         .stream()
                         .map(filterId -> filterMap.get(filterId))
                         .collect(toImmutableSet());
    }

    @Override
    public void addColumnToFilterList(@Nonnull String columnName,
                                      @Nonnull GridColumnId columnId) {
        FilterId filterId = new FilterId(columnName);
        filterView.addFilter(filterId, FilterSetting.ON);
        filterMap.put(filterId, columnId);
    }
}
