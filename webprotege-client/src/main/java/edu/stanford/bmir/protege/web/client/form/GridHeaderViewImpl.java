package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.filter.FilterViewPopup;
import edu.stanford.bmir.protege.web.client.library.popupmenu.MenuButton;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderViewImpl extends Composite implements GridHeaderView {

    private Map<FilterId, GridColumnId> filterMap = new HashMap<>();

    private GridHeaderPresenter.GridColumnVisibilityChangedHandler columnVisibilityChangedHandler = () -> {};

    interface GridHeaderViewImplUiBinder extends UiBinder<HTMLPanel, GridHeaderViewImpl> {

    }

    private static GridHeaderViewImplUiBinder ourUiBinder = GWT.create(GridHeaderViewImplUiBinder.class);

    @UiField
    HTMLPanel headerContainer;

    @UiField
    MenuButton menuButton;

    private FilterView filterView;

    private FilterViewPopup filterViewPopup = new FilterViewPopup();

    @Inject
    public GridHeaderViewImpl(FilterView filterView) {
        this.filterView = checkNotNull(filterView);
        initWidget(ourUiBinder.createAndBindUi(this));
        menuButton.addClickHandler(this::handleMenuButtonClicked);
    }

    private void handleMenuButtonClicked(@Nonnull ClickEvent event) {
        filterViewPopup.showFilterView(filterView,
                                       menuButton,
                                       this::handleColumnFilterChanged);
    }

    private void handleColumnFilterChanged(FilterSet filterSet) {
        columnVisibilityChangedHandler.handleColumnVisibilityChanged();
    }

    @Override
    public void addColumnHeader(@Nonnull IsWidget headerWidget, double weight) {
        checkNotNull(headerWidget);
        Style style = headerWidget.asWidget().getElement().getStyle();
        style.setProperty("flexBasis", weight * 100, Style.Unit.PCT);
        headerContainer.add(headerWidget);
    }

    @Override
    public void clear() {
        headerContainer.clear();
        filterMap.clear();
    }

    @Override
    public void addColumnToFilterList(@Nonnull String columnName,
                                      @Nonnull GridColumnId columnId) {
        FilterId filterId = new FilterId(columnName);
        filterView.addFilter(filterId, FilterSetting.ON);
        filterMap.put(filterId, columnId);
    }

    @Override
    public ImmutableList<GridColumnId> getVisibleColumns() {
        return filterView.getFilterSet()
                         .getOnFilters()
                         .stream()
                         .map(filterId -> filterMap.get(filterId))
                         .collect(toImmutableList());
    }

    @Override
    public void setGridColumnVisibilityChangedHandler(@Nonnull GridHeaderPresenter.GridColumnVisibilityChangedHandler handler) {
        this.columnVisibilityChangedHandler = checkNotNull(handler);
    }

    @Override
    public void setColumnVisible(int columnIndex, boolean visible) {
        this.headerContainer.getWidget(columnIndex).setVisible(visible);
    }
}
