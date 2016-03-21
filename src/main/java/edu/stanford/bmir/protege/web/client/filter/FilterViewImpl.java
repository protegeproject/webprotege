package edu.stanford.bmir.protege.web.client.filter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.filter.Filter;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private List<FilterCheckBox> currentGroup = new ArrayList<>();

    public FilterViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }



    @Override
    public void addFilterGroup(String filterGroup) {
        filterGroupHeader = new FilterGroupHeader();
        filterGroupHeader.setLabel(filterGroup);
        container.add(filterGroupHeader);
    }

    @Override
    public void closeCurrentGroup() {
        if(filterGroupHeader == null) {
            return;
        }
        final List<FilterCheckBox> currentGroupCopy = new ArrayList<>(currentGroup);
        filterGroupHeader.setSelectAllClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                handleSelect(currentGroupCopy, FilterSetting.ON);
            }
        });
        filterGroupHeader.setSelectNoneClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                handleSelect(currentGroupCopy, FilterSetting.OFF);
            }
        });
    }

    private void handleSelect(List<FilterCheckBox> group, FilterSetting setting) {
        for(FilterCheckBox checkBox : group) {
            checkBox.setSetting(setting);
        }
    }

    @Override
    public void addFilter(FilterId filterId, FilterSetting initialSetting) {
        FilterCheckBox checkBox = new FilterCheckBox();
        checkBox.setFilterId(filterId);
        checkBox.setSetting(initialSetting);
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