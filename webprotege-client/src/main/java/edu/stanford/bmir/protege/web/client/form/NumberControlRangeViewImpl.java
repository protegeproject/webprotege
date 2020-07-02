package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlRange;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class NumberControlRangeViewImpl extends Composite implements NumberControlRangeView {

    interface NumberRangeViewImplUiBinder extends UiBinder<HTMLPanel, NumberControlRangeViewImpl> {

    }

    private static NumberRangeViewImplUiBinder ourUiBinder = GWT.create(NumberRangeViewImplUiBinder.class);

    @UiField
    RadioButton anyNumberRadio;

    @UiField
    RadioButton specificRangeRadio;

    @UiField
    TextBox lowerBound;

    @UiField
    ListBox lowerBoundType;

    @UiField
    TextBox upperBound;

    @UiField
    ListBox upperBoundType;

    @UiField(provided = true)
    static Counter counter = new Counter();

    @Inject
    public NumberControlRangeViewImpl() {
        counter.increment();
        initWidget(ourUiBinder.createAndBindUi(this));
        lowerBound.addValueChangeHandler(this::handleBoundChanged);
    }

    private void handleBoundChanged(ValueChangeEvent<String> event) {
        setAnyNumber(false);
    }

    @Override
    public void clear() {
        anyNumberRadio.setValue(true);
        lowerBound.setText("");
        lowerBoundType.setSelectedIndex(0);
        upperBound.setText("");
        upperBoundType.setSelectedIndex(0);
    }

    @Override
    public void setAnyNumber(boolean b) {
        if(b) {
            anyNumberRadio.setValue(true);
        }
        else {
            specificRangeRadio.setValue(true);
        }
    }

    @Override
    public boolean isAnyNumber() {
        return anyNumberRadio.getValue();
    }

    @Override
    public void setLowerBound(double min) {
        lowerBound.setText(NumberFormat.getDecimalFormat().format(min));
    }

    @Override
    public double getLowerBound() {
        try {
            return NumberFormat.getDecimalFormat().parse(lowerBound.getText());
        } catch(NumberFormatException e) {
            return Double.MIN_VALUE;
        }
    }

    @Override
    public void setLowerBoundType(NumberControlRange.BoundType boundType) {
        lowerBoundType.setSelectedIndex(boundType.ordinal());
    }

    @Override
    public NumberControlRange.BoundType getLowerBoundType() {
        return NumberControlRange.BoundType.values()[lowerBoundType.getSelectedIndex()];
    }

    @Override
    public void setUpperBound(double max) {
        upperBound.setText(NumberFormat.getDecimalFormat().format(max));
    }

    @Override
    public double getUpperBound() {
        try {
            return NumberFormat.getDecimalFormat().parse(upperBound.getValue());
        } catch(NumberFormatException e) {
            // TODO
            return Double.MAX_VALUE;
        }
    }

    @Override
    public void setUpperBoundType(NumberControlRange.BoundType boundType) {
        upperBoundType.setSelectedIndex(boundType.ordinal());
    }

    @Override
    public NumberControlRange.BoundType getUpperBoundType() {
        return NumberControlRange.BoundType.values()[upperBoundType.getSelectedIndex()];
    }
}
