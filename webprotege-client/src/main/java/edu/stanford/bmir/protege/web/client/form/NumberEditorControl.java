package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlDescriptorDto;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlRange;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2017
 */
public class NumberEditorControl extends Composite implements FormControl, HasPlaceholder {

    private NumberFormat format = NumberFormat.getFormat("0.00");

    private NumberControlRange range = NumberControlRange.all();

    private NumberControlDescriptorDto descriptor;

    private Optional<OWLLiteral> currentValue = Optional.empty();

    public void setDescriptor(NumberControlDescriptorDto formFieldDescriptor) {
        this.descriptor = formFieldDescriptor;
        setFormat(formFieldDescriptor.getFormat());
        setRange(formFieldDescriptor.getRange());
        setLength(formFieldDescriptor.getLength());
        LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
        String localeName = localeInfo.getLocaleName();
        String placeholder = formFieldDescriptor.getPlaceholder()
                                                .get(localeName);
        setPlaceholder(placeholder);
        numberField.addValueChangeHandler(this::handleValueChanged);
    }

    private void handleValueChanged(ValueChangeEvent<String> event) {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    interface NumberEditorControlUiBinder extends UiBinder<HTMLPanel, NumberEditorControl> {

    }

    private static NumberEditorControlUiBinder ourUiBinder = GWT.create(NumberEditorControlUiBinder.class);

    @UiField
    TextBox numberField;

    @UiField
    Label errorLabel;

    private boolean dirty = false;

    @Inject
    public NumberEditorControl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        numberField.setVisibleLength(10);
    }

    @Override
    public void requestFocus() {
        numberField.setFocus(true);
    }

    @Override
    public void setValue(@Nonnull FormControlDataDto object) {
        if (!(object instanceof NumberControlDataDto)) {
            clearValue();
            return;
        }
        NumberControlDataDto data = (NumberControlDataDto) object;
        try {
            currentValue = data.getValue();
            if(currentValue.isPresent()) {
                numberField.setText(currentValue.get().getLiteral());
            }
            else {
                numberField.setText("");
            }
            dirty = false;
        } catch(NumberFormatException e) {
            clearValue();
        }
        validate();
    }

    @Override
    public void clearValue() {
        numberField.setText("");
        currentValue = Optional.empty();
        dirty = false;
    }

    @Nonnull
    @Override
    public ImmutableSet<FormRegionFilter> getFilters() {
        return ImmutableSet.of();
    }

    @Override
    public Optional<FormControlData> getValue() {
        try {
            if(dirty) {
                OWLLiteral v = DataFactory.getOWLLiteral(numberField.getText().trim(),
                                                         DataFactory.getXSDDecimal());
                GWT.log("[NumberEditorControl] Value: " + v);
                return Optional.of(NumberControlData.get(descriptor.getDescriptor(), v));
            }
            else {
                return currentValue.map(v -> NumberControlData.get(descriptor.getDescriptor(), v));

            }
        } catch (NumberFormatException e) {
            GWT.log("[NumberEditorControl] Invalid number format (" + format.getPattern() + ") " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormControlData>> handler) {
        GWT.log("[NumberEditorControl] addValueChangeHandler");
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void setLength(int length) {
        numberField.setVisibleLength(length);
    }

    public void setFormat(@Nonnull String numberFormat) {
        try {
            GWT.log("[NumberEditorControl] Set number format to " + numberFormat);
            format = NumberFormat.getFormat(checkNotNull(numberFormat));
            validate();
        } catch(NumberFormatException e) {
            GWT.log("[NumberEditorControl] Invalid number format (" + numberFormat + "): " + e.getMessage());
        }
    }

    public void setRange(NumberControlRange numberRange) {
        GWT.log("[NumberEditorControl] Setting range to " + numberRange);
        this.range = checkNotNull(numberRange);
    }

    private boolean reformat() {
        try {
            double v = format.parse(numberField.getText().trim());
            numberField.setText(format.format(v));
            return true;
        } catch (NumberFormatException e) {
            displayErrorMessage("[NumberEditorControl] Invalid number");
            return false;
        }
    }

    @UiHandler("numberField")
    public void numberFieldChange(ChangeEvent event) {
        clearErrorMessage();
        if(reformat()) {
            validate();
        }
        GWT.log("[NumberEditorControl] ValueChangeEvent");
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
        String trimmedText = numberField.getText()
                                        .trim();
        if(trimmedText.isEmpty()) {
            clearErrorMessage();
            return;
        }
        try {
            format.parse(trimmedText);
        } catch(NumberFormatException e) {
            displayErrorMessage("Incorrect number format");
        }
        Range<Double> r = range.toRange();
        double v = format.parse(trimmedText);
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
        numberField.removeStyleName(BUNDLE.style().errorBorder());
        numberField.addStyleName(BUNDLE.style().noErrorBorder());
    }

    private void displayErrorMessage(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
        numberField.removeStyleName(BUNDLE.style().noErrorBorder());
        numberField.addStyleName(BUNDLE.style().errorBorder());
    }

    private String formatRange() {
        String msg = "";
        double lowerBound = range.getLowerBound();
        if(lowerBound != Double.MIN_VALUE) {
            String lower = format.format(lowerBound);
            msg = range.getLowerBoundType().getLowerSymbol() + " " + lower;
        }
        double upperBound = range.getUpperBound();
        if(upperBound != Double.MAX_VALUE) {
            if(lowerBound != Double.MIN_VALUE) {
                msg += " and ";
            }
            String upper = range.getUpperBoundType().getUpperSymbol() + " " + format.format(upperBound);
            msg += upper;
        }
        return  msg;
    }

    @Override
    public void setEnabled(boolean enabled) {
        numberField.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return numberField.isEnabled();
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {

    }
}
