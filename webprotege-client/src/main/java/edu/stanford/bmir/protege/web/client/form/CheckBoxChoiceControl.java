package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.MultiChoiceControlData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.SingleChoiceControlData;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.MultiChoiceControlDescriptor;

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

    private MultiChoiceControlDescriptor descriptor;

    interface CheckBoxChoiceControlUiBinder extends UiBinder<HTMLPanel, CheckBoxChoiceControl> {

    }

    private static CheckBoxChoiceControlUiBinder ourUiBinder = GWT.create(CheckBoxChoiceControlUiBinder.class);

    @UiField
    HTMLPanel container;

    private final Map<CheckBox, ChoiceDescriptor> checkBoxes = new LinkedHashMap<>();

    private final List<PrimitiveFormControlData> defaultChoices = new ArrayList<>();

    @Inject
    public CheckBoxChoiceControl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        checkBoxValueChangedHandler = event -> ValueChangeEvent.fire(CheckBoxChoiceControl.this, getValue());
    }

    public void setDescriptor(@Nonnull MultiChoiceControlDescriptor descriptor) {
        this.descriptor = checkNotNull(descriptor);
        setChoices(descriptor.getChoices());
        setDefaultChoices(descriptor.getDefaultChoices());
    }

    private void setChoices(List<ChoiceDescriptor> choices) {
        container.clear();
        checkBoxes.clear();
        String langTag = LocaleInfo.getCurrentLocale().getLocaleName();
        for(ChoiceDescriptor choiceDescriptor : choices) {
            CheckBox checkBox = new CheckBox(new SafeHtmlBuilder().appendHtmlConstant(choiceDescriptor.getLabel().get(langTag)).toSafeHtml());
            checkBoxes.put(checkBox, choiceDescriptor);
            container.add(checkBox);
            checkBox.getElement().getStyle().setDisplay(Style.Display.BLOCK);
            checkBox.addValueChangeHandler(checkBoxValueChangedHandler);
            checkBox.addStyleName(WebProtegeClientBundle.BUNDLE.style().noFocusBorder());
            checkBox.addFocusHandler(event -> {
                checkBox.addStyleName(WebProtegeClientBundle.BUNDLE.style().focusBorder());
                checkBox.removeStyleName(WebProtegeClientBundle.BUNDLE.style().noFocusBorder());
            });
            checkBox.addBlurHandler(event -> {
                checkBox.addStyleName(WebProtegeClientBundle.BUNDLE.style().noFocusBorder());
                checkBox.removeStyleName(WebProtegeClientBundle.BUNDLE.style().focusBorder());
            });
        }
    }

    private void setDefaultChoices(List<ChoiceDescriptor> defaultChoices) {
        this.defaultChoices.clear();
        defaultChoices.stream()
                      .map(ChoiceDescriptor::getValue)
                      .forEach(this.defaultChoices::add);
        selectDefaultChoices();
    }

    @Override
    public void setValue(FormControlData value) {
        // Should be a SingleChoice
        if(value instanceof SingleChoiceControlData) {
            SingleChoiceControlData singleChoiceControlData = (SingleChoiceControlData) value;
            for(CheckBox checkBox : checkBoxes.keySet()) {
                checkBox.setValue(singleChoiceControlData.getChoice().equals(Optional.of(checkBoxes.get(checkBox).getValue())));
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
    }

    @Override
    public Optional<FormControlData> getValue() {
        ImmutableList<PrimitiveFormControlData> selectedValues = checkBoxes.keySet()
                                                                  .stream()
                                                                  .filter(CheckBox::getValue)
                                                                  .map(checkBoxes::get)
                                                                  .map(ChoiceDescriptor::getValue)
                                                                  .collect(toImmutableList());
        return Optional.of(MultiChoiceControlData.get(descriptor, selectedValues));
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormControlData>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }

    @Override
    public void requestFocus() {
        checkBoxes.keySet()
                  .stream()
                  .findFirst()
                  .ifPresent(checkBox -> checkBox.setFocus(true));
    }
}
