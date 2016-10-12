package edu.stanford.bmir.protege.web.client.filter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.filter.Filter;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public class FilterViewImpl extends Composite implements FilterView {

    private FilterGroupHeader filterGroupHeader;

    interface FilterViewImplUiBinder extends UiBinder<HTMLPanel, FilterViewImpl> {

    }

    private static FilterViewImplUiBinder ourUiBinder = GWT.create(FilterViewImplUiBinder.class);

    @UiField
    HTMLPanel container;

    private FilterIdRenderer renderer = filterId -> filterId.getLabel();

    private List<FilterCheckBox> currentGroup = new ArrayList<>();

    @Inject
    public FilterViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setFilterIdRenderer(FilterIdRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void addFilterGroup(String filterGroup) {
        closeCurrentGroup();
        filterGroupHeader = new FilterGroupHeader();
        filterGroupHeader.setLabel(filterGroup);
        container.add(filterGroupHeader);
    }

    @Override
    public void closeCurrentGroup() {
        if(filterGroupHeader == null) {
            currentGroup.clear();
            return;
        }
        final List<FilterCheckBox> currentGroupCopy = new ArrayList<>(currentGroup);
        filterGroupHeader.setSelectAllClickHandler(event -> handleSelect(currentGroupCopy, FilterSetting.ON));
        filterGroupHeader.setSelectNoneClickHandler(event -> handleSelect(currentGroupCopy, FilterSetting.OFF));
        currentGroup.clear();
        filterGroupHeader = null;
    }

    private void handleSelect(List<FilterCheckBox> group, FilterSetting setting) {
        for(FilterCheckBox checkBox : group) {
            checkBox.setSetting(setting);
        }
        ValueChangeEvent.fire(this, getFilterSet());
    }

    @Override
    public void addFilter(FilterId filterId, FilterSetting initialSetting) {
        FilterCheckBox checkBox = new FilterCheckBox();
        checkBox.setFilterId(filterId, renderer.render(filterId));
        checkBox.setSetting(initialSetting);
        checkBox.addValueChangeHandler((v) -> ValueChangeEvent.fire(this, getFilterSet()));
        container.add(checkBox);
        currentGroup.add(checkBox);
    }

    @Override
    public FilterSet getFilterSet() {
        return new FilterSet(getFilters());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<FilterSet> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public List<Filter> getFilters() {
        List<Filter> result = new ArrayList<>();
        for(int i = 0; i < container.getWidgetCount(); i++) {
            Widget w = container.getWidget(i);
            if(w instanceof FilterCheckBox) {
                FilterCheckBox cb = (FilterCheckBox) w;
                result.add(new Filter(cb.getFilterId(), cb.getSetting()));
            }
        }
        return result;
    }

}