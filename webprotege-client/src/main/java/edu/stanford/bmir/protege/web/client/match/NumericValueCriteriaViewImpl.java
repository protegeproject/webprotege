package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.shared.match.criteria.NumericPredicate;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class NumericValueCriteriaViewImpl extends Composite implements NumericValueCriteriaView {

    private static final NumberFormat DECIMAL_FORMAT = NumberFormat.getDecimalFormat();

    interface NumericValueCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, NumericValueCriteriaViewImpl> {

    }

    private static NumericValueCriteriaViewImplUiBinder ourUiBinder = GWT.create(NumericValueCriteriaViewImplUiBinder.class);

    @UiField
    ListBox numericPredicateField;

    @UiField
    TextBox valueField;

    @Inject
    public NumericValueCriteriaViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        for(NumericPredicate predicate : NumericPredicate.values()) {
            numericPredicateField.addItem(predicate.getSymbol(), predicate.name());
        }
        numericPredicateField.setSelectedIndex(0);
    }

    @UiHandler("valueField")
    protected void handleValueChanged(ValueChangeEvent<String> event) {
            getValue().ifPresent(d -> valueField.setText(DECIMAL_FORMAT.format(d)));
    }

    private double parseEnteredValue() {
        return DECIMAL_FORMAT.parse(valueField.getText().trim());
    }

    @Nonnull
    @Override
    public Optional<Double> getValue() {
        try {
            return Optional.of(parseEnteredValue());
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public void setValue(double value) {
        valueField.setText(Double.toString(value));
    }

    @Nonnull
    @Override
    public NumericPredicate getNumericPredicate() {
        String selectedValue = numericPredicateField.getSelectedValue();
        return NumericPredicate.valueOf(selectedValue);
    }

    @Override
    public void setNumericPredicate(@Nonnull NumericPredicate predicate) {
        numericPredicateField.setSelectedIndex(predicate.ordinal());
    }

    @Override
    public void clear() {
        numericPredicateField.clear();
    }
}