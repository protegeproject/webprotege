package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.NumberFieldRange;
import org.apache.commons.lang.math.NumberRange;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2017
 */
public class NumberFieldEditor extends Composite implements ValueEditor<FormDataValue>, FormElementEditor, HasPlaceholder {

    private NumberFormat format = NumberFormat.getFormat("0.00");

    private NumberFieldRange range = NumberFieldRange.all();

    interface NumberFieldEditorUiBinder extends UiBinder<HTMLPanel, NumberFieldEditor> {

    }

    private static NumberFieldEditorUiBinder ourUiBinder = GWT.create(NumberFieldEditorUiBinder.class);

    @UiField
    TextBox numberField;

    @UiField
    Label errorLabel;

    public NumberFieldEditor() {
        initWidget(ourUiBinder.createAndBindUi(this));
        numberField.setVisibleLength(10);
    }

    @Override
    public boolean isWellFormed() {
        return getValue().isPresent();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public void setValue(FormDataValue object) {
        if (!(object instanceof FormDataPrimitive)) {
            clearValue();
            return;
        }
        if (!((FormDataPrimitive) object).isNumber()) {
            clearValue();
            return;
        }
        double v = ((FormDataPrimitive) object).getValueAsDouble();
        numberField.setText(format.format(v));
        validate();
    }

    @Override
    public void clearValue() {
        numberField.setText("");
    }

    @Override
    public Optional<FormDataValue> getValue() {
        try {
            double v = format.parse(numberField.getText().trim());
            return Optional.of(FormDataPrimitive.get(v));
        } catch (NumberFormatException e) {
            GWT.log("Invalid format " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataValue>> handler) {
        GWT.log("[NumberFieldEditor] addValueChangeHandler");
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    public void setLength(int length) {
        numberField.setVisibleLength(length);
    }

    public void setFormat(@Nonnull String numberFormat) {
        format = NumberFormat.getFormat(checkNotNull(numberFormat));
    }

    public void setRange(NumberFieldRange numberRange) {
        GWT.log("Setting range to " + numberRange);
        this.range = checkNotNull(numberRange);
    }

    private boolean reformat() {
        try {
            double v = format.parse(numberField.getText().trim());
            numberField.setText(format.format(v));
            return true;
        } catch (NumberFormatException e) {
            displayErrorMessage("Invalid number");
            return false;
        }
    }

    @UiHandler("numberField")
    public void numberFieldChange(ChangeEvent event) {
        clearErrorMessage();
        if(reformat()) {
            validate();
        }
        GWT.log("[NumberFieldEditor] ValueChangeEvent");
        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    public String getPlaceholder() {
        String placeholder = numberField.getElement().getAttribute("placeholder");
        return placeholder == null ? "" : placeholder;
    }

    @Override
    public void setPlaceholder(String placeholder) {
        numberField.getElement().setAttribute("placeholder", placeholder);
    }

    private void validate() {
        Range<Double> r = range.toRange();
        double v = format.parse(numberField.getText().trim());
        if(!r.contains(v)) {
            displayErrorMessage("Value must be " + formatRange());
        }
        else {
            clearErrorMessage();
        }
    }

    private void clearErrorMessage() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        numberField.getElement().getStyle().clearBorderColor();
    }

    private void displayErrorMessage(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
        numberField.getElement().getStyle().setBorderColor("#ff0000");
    }

    private String formatRange() {
        String lower = format.format(range.getLowerBound());
        String upper = format.format(range.getUpperBound());
        return range.getLowerBoundType().getLowerSymbol() + " " + lower + " and " + range.getUpperBoundType().getUpperSymbol() + " " + upper;
    }

}