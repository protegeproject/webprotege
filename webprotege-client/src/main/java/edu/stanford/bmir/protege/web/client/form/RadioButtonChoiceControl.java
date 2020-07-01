package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class RadioButtonChoiceControl extends Composite implements SingleChoiceControl {

    private static int nameCounter = 0;

    private ValueChangeHandler<Boolean> radioButtonValueChangedHandler;

    private SingleChoiceControlDescriptorDto descriptor;

    private boolean enabled = true;

    interface RadioButtonChoiceControlUiBinder extends UiBinder<HTMLPanel, RadioButtonChoiceControl> {

    }

    private static RadioButtonChoiceControlUiBinder ourUiBinder = GWT.create(RadioButtonChoiceControlUiBinder.class);

    @UiField
    HTMLPanel container;

    private Map<RadioButton, PrimitiveFormControlData> choiceButtons = new LinkedHashMap<>();

    private List<PrimitiveFormControlData> choices = new ArrayList<>();

    private Optional<PrimitiveFormControlData> defaultChoice = Optional.empty();

    @Inject
    public RadioButtonChoiceControl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        radioButtonValueChangedHandler = event -> ValueChangeEvent.fire(RadioButtonChoiceControl.this, getValue());
    }

    @Override
    public void setDescriptor(@Nonnull SingleChoiceControlDescriptorDto descriptor) {
        this.descriptor = descriptor;
        setChoices(descriptor.getAvailableChoices());
//        descriptor.getDefaultChoice().ifPresent(this::setDefaultChoice);

    }

    private void setChoices(List<ChoiceDescriptorDto> choiceDescriptorDtos) {
        List<PrimitiveFormControlData> nextChoices = choiceDescriptorDtos.stream()
                .map(ChoiceDescriptorDto::getValue)
                .map(PrimitiveFormControlDataDto::toPrimitiveFormControlData)
                .collect(toImmutableList());
        if(this.choices.equals(nextChoices)) {
            return;
        }
        container.clear();
        choiceButtons.clear();
        choices.clear();
        nameCounter++;
        String langTag = LocaleInfo.getCurrentLocale().getLocaleName();
        for(ChoiceDescriptorDto descriptorDto : choiceDescriptorDtos) {
            RadioButton radioButton = new RadioButton("Choice" + nameCounter, new SafeHtmlBuilder().appendHtmlConstant(descriptorDto.getLabel().get(langTag)).toSafeHtml());
            radioButton.addStyleName(WebProtegeClientBundle.BUNDLE.style().noFocusBorder());
            radioButton.addValueChangeHandler(radioButtonValueChangedHandler);
            radioButton.addFocusHandler(event -> {
                radioButton.addStyleName(WebProtegeClientBundle.BUNDLE.style().focusBorder());
                radioButton.removeStyleName(WebProtegeClientBundle.BUNDLE.style().noFocusBorder());
            });
            radioButton.addBlurHandler(event -> {
                radioButton.addStyleName(WebProtegeClientBundle.BUNDLE.style().noFocusBorder());
                radioButton.removeStyleName(WebProtegeClientBundle.BUNDLE.style().focusBorder());
            });
            choices.add(descriptorDto.getValue().toPrimitiveFormControlData());
            container.add(radioButton);
            choiceButtons.put(radioButton, descriptorDto.getValue().toPrimitiveFormControlData());
        }
        selectDefaultChoice();
    }

    private void setDefaultChoice(@Nonnull ChoiceDescriptor choice) {
        defaultChoice = Optional.of(choice.getValue());
    }

    private void selectDefaultChoice() {

    }

    @Override
    public void setValue(@Nonnull FormControlDataDto value) {
        if(value instanceof SingleChoiceControlDataDto) {
            SingleChoiceControlDataDto choiceControlDataDto = (SingleChoiceControlDataDto) value;
            Optional<PrimitiveFormControlData> choice = choiceControlDataDto.getChoice().map(PrimitiveFormControlDataDto::toPrimitiveFormControlData);
            setChoice(choice);
        }
        else {
            clearValue();
        }
    }

    public void setChoice(Optional<PrimitiveFormControlData> choice) {
        if(choice.isPresent()) {
            for(RadioButton radioButton : choiceButtons.keySet()) {
                PrimitiveFormControlData value = choiceButtons.get(radioButton);
                if(value.equals(choice.get())) {
                    radioButton.setValue(true);
                }
            }
        }
        else {
            clearValue();
        }
    }

    @Override
    public void clearValue() {
        for(RadioButton radioButton : choiceButtons.keySet()) {
            radioButton.setValue(false);
        }
        selectDefaultChoice();
    }

    @Nonnull
    @Override
    public ImmutableSet<FormRegionFilter> getFilters() {
        return ImmutableSet.of();
    }

    @Override
    public Optional<FormControlData> getValue() {
        for(RadioButton radioButton : choiceButtons.keySet()) {
            if(radioButton.getValue()) {
                PrimitiveFormControlData value = choiceButtons.get(radioButton);
                return Optional.of(SingleChoiceControlData.get(descriptor.toFormControlDescriptor(), value));
            }
        }
        return Optional.empty();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormControlData>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void requestFocus() {
        choiceButtons.keySet()
                     .stream()
                     .findFirst()
                     .ifPresent(radioButton -> radioButton.setFocus(true));
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        choiceButtons.keySet()
                     .forEach(radioButton -> radioButton.setEnabled(enabled));
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {

    }
}
