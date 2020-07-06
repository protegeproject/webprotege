package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.form.input.CheckBox;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class CheckBoxChoiceControl extends Composite implements MultiValueChoiceControl {

    private ValueChangeHandler<Boolean> checkBoxValueChangedHandler;

    private MultiChoiceControlDescriptorDto descriptor;

    private boolean enabled = true;

    interface CheckBoxChoiceControlUiBinder extends UiBinder<HTMLPanel, CheckBoxChoiceControl> {

    }

    private static CheckBoxChoiceControlUiBinder ourUiBinder = GWT.create(CheckBoxChoiceControlUiBinder.class);

    @UiField
    HTMLPanel container;

    private final Map<CheckBox, PrimitiveFormControlData> checkBoxes = new LinkedHashMap<>();

    private final List<PrimitiveFormControlData> choices = new ArrayList<>();

    private final List<PrimitiveFormControlData> defaultChoices = new ArrayList<>();

    @Inject
    public CheckBoxChoiceControl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        checkBoxValueChangedHandler = event -> handleCheckBoxValueChanged();
    }

    public void handleCheckBoxValueChanged() {
        ValueChangeEvent.fire(CheckBoxChoiceControl.this, getValue());
    }

    public void setDescriptor(@Nonnull MultiChoiceControlDescriptorDto descriptor) {
        this.descriptor = checkNotNull(descriptor);
//        setDefaultChoices(descriptor.getDefaultChoices());
        setChoices(descriptor.getAvailableChoices());
    }

    private void setChoices(List<ChoiceDescriptorDto> choiceDtos) {
        List<PrimitiveFormControlData> nextChoices = choiceDtos.stream().map(d -> d.getValue().toPrimitiveFormControlData()).collect(toImmutableList());
        if(this.choices.equals(nextChoices)) {
            return;
        }
        container.clear();
        checkBoxes.clear();
        choices.clear();
        String langTag = LocaleInfo.getCurrentLocale().getLocaleName();
        for(ChoiceDescriptorDto choiceDescriptorDto : choiceDtos) {
            CheckBox checkBox = new CheckBox();
            checkBox.setText(choiceDescriptorDto.getLabel().get(langTag));
            checkBoxes.put(checkBox, choiceDescriptorDto.getValue().toPrimitiveFormControlData());
            container.add(checkBox);
            choices.add(choiceDescriptorDto.getValue().toPrimitiveFormControlData());
            checkBox.addValueChangeHandler(checkBoxValueChangedHandler);
        }
        ValueChangeEvent.fire(this, getValue());
    }

    private void setDefaultChoices(List<ChoiceDescriptor> defaultChoices) {
        this.defaultChoices.clear();
        defaultChoices.stream()
                      .map(ChoiceDescriptor::getValue)
                      .forEach(this.defaultChoices::add);
        selectDefaultChoices();
    }

    @Override
    public void setValue(@Nonnull FormControlDataDto value) {
        if(value instanceof MultiChoiceControlDataDto) {
            MultiChoiceControlDataDto multiChoiceControlData = (MultiChoiceControlDataDto) value;
            ImmutableList<PrimitiveFormControlData> values = multiChoiceControlData.getValues().stream().map(
                    PrimitiveFormControlDataDto::toPrimitiveFormControlData).collect(toImmutableList());
            for(CheckBox checkBox : checkBoxes.keySet()) {
                boolean sel = values.contains(checkBoxes.get(checkBox));
                checkBox.setValue(sel);
                checkBox.setEnabled(enabled);
            }
        }
        else {
            clearValue();
        }
    }

    private void selectDefaultChoices() {
        // TODO
    }

    @Override
    public void clearValue() {
        for(CheckBox checkBox : checkBoxes.keySet()) {
            checkBox.setValue(false);
        }
        ValueChangeEvent.fire(this, getValue());
    }

    @Nonnull
    @Override
    public ImmutableSet<FormRegionFilter> getFilters() {
        return ImmutableSet.of();
    }

    @Override
    public Optional<FormControlData> getValue() {
        if(checkBoxes.isEmpty()) {
            return Optional.empty();
        }
        ImmutableList<PrimitiveFormControlData> selectedValues = checkBoxes.keySet()
                                                                  .stream()
                                                                  .filter(CheckBox::getValue)
                                                                  .map(checkBoxes::get)
                                                                  .collect(toImmutableList());
        if(selectedValues.isEmpty()) {
            return Optional.empty();
        }
        else {
            return Optional.of(MultiChoiceControlData.get(descriptor.toFormControlDescriptor(), selectedValues));
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormControlData>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void requestFocus() {
        checkBoxes.keySet()
                  .stream()
                  .findFirst()
                  .ifPresent(checkBox -> checkBox.setFocus(true));
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        checkBoxes.keySet()
                  .forEach(checkBox -> checkBox.setEnabled(enabled));
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {

    }
}
