package edu.stanford.bmir.protege.web.client.filter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public class FilterCheckBox extends Composite implements HasValueChangeHandlers<Boolean> {

    interface FilterCheckBoxUiBinder extends UiBinder<HTMLPanel, FilterCheckBox> {

    }

    private static FilterCheckBoxUiBinder ourUiBinder = GWT.create(FilterCheckBoxUiBinder.class);

    @UiField
    CheckBox checkBox;

    private FilterId filterId;

    public FilterCheckBox() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setFilterId(FilterId id, String rendering) {
        this.filterId = id;
        checkBox.setText(rendering);
    }

    @UiHandler("checkBox")
    protected void handleCheckBoxValueChanged(ValueChangeEvent<Boolean> event) {
        ValueChangeEvent.fire(this, checkBox.getValue());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public FilterId getFilterId() {
        return filterId;
    }

    public void setSetting(FilterSetting filterSetting) {
        if(filterSetting == FilterSetting.ON) {
            checkBox.setValue(true);
        }
        else {
            checkBox.setValue(false);
        }
    }

    public FilterSetting getSetting() {
        if(checkBox.getValue()) {
            return FilterSetting.ON;
        }
        else {
            return FilterSetting.OFF;
        }
    }
}