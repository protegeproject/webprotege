package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.SingleChoiceControlData;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class ComboBoxChoiceControl extends Composite implements SingleChoiceControl {

    private SingleChoiceControlDescriptor descriptor;

    interface ComboBoxChoiceControlUiBinder extends UiBinder<HTMLPanel, ComboBoxChoiceControl> {

    }

    private static ComboBoxChoiceControlUiBinder ourUiBinder = GWT.create(ComboBoxChoiceControlUiBinder.class);

    @UiField
    ListBox comboBox;

    private final List<ChoiceDescriptor> choiceDescriptors = new ArrayList<>();

    private Optional<PrimitiveFormControlData> defaultChoice = Optional.empty();

    @Inject
    public ComboBoxChoiceControl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("comboBox")
    protected void handleValueChanged(ChangeEvent changeEvent) {
        ValueChangeEvent.fire(this, getValue());
    }

    public void setChoices(List<ChoiceDescriptor> choices) {
        comboBox.clear();
        choiceDescriptors.clear();
        comboBox.addItem("");
        String langTag = LocaleInfo.getCurrentLocale().getLocaleName();
        for(ChoiceDescriptor descriptor : choices) {
            choiceDescriptors.add(descriptor);
            comboBox.addItem(descriptor.getLabel().get(langTag));
        }
        selectDefaultChoice();
    }

    @Override
    public void setDescriptor(@Nonnull SingleChoiceControlDescriptor descriptor) {
        this.descriptor = descriptor;
        setChoices(descriptor.getChoices());
        descriptor.getDefaultChoice().ifPresent(this::setDefaultChoice);
    }

    private void setDefaultChoice(@Nonnull ChoiceDescriptor defaultChoice) {
        this.defaultChoice = Optional.of(defaultChoice.getValue());
    }

    private void selectDefaultChoice() {
        comboBox.setSelectedIndex(0);
    }

    @Override
    public void setValue(FormControlData value) {
        clearValue();
        if(value instanceof SingleChoiceControlData) {
            Optional<PrimitiveFormControlData> choice = ((SingleChoiceControlData) value).getChoice();
            if(choice.isPresent()) {
                int index = 1;
                for(ChoiceDescriptor descriptor : choiceDescriptors) {
                    if(descriptor.getValue().equals(choice.get())) {
                        comboBox.setSelectedIndex(index);
                        break;
                    }
                    index++;
                }
            }
        }
    }

    @Override
    public void clearValue() {
        selectDefaultChoice();
    }

    @Override
    public Optional<FormControlData> getValue() {
        int selIndex = comboBox.getSelectedIndex();
        if(selIndex < 1) {
            return Optional.empty();
        }
        PrimitiveFormControlData choice = choiceDescriptors.get(selIndex - 1).getValue();
        return Optional.of(SingleChoiceControlData.get(descriptor, choice));
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
        return false;
    }

    @Override
    public void requestFocus() {
        comboBox.setFocus(true);
    }
}
