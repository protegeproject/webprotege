package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class ChoiceFieldComboBoxEditor extends Composite implements ChoiceFieldEditor {

    interface ChoiceFieldComboBoxEditorUiBinder extends UiBinder<HTMLPanel, ChoiceFieldComboBoxEditor> {

    }

    private static ChoiceFieldComboBoxEditorUiBinder ourUiBinder = GWT.create(ChoiceFieldComboBoxEditorUiBinder.class);

    @UiField
    ListBox comboBox;

    private final List<ChoiceDescriptor> choiceDescriptors = new ArrayList<>();


    public ChoiceFieldComboBoxEditor() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("comboBox")
    protected void handleValueChanged(ChangeEvent changeEvent) {
        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    public void setChoices(List<ChoiceDescriptor> choices) {
        comboBox.clear();
        choiceDescriptors.clear();
        comboBox.addItem("");
        for(ChoiceDescriptor descriptor : choices) {
            choiceDescriptors.add(descriptor);
            comboBox.addItem(descriptor.getLabel());
        }
    }

    @Override
    public void setValue(FormDataValue value) {
        int index = 1;
        for(ChoiceDescriptor descriptor : choiceDescriptors) {
            if(descriptor.getValue().equals(value)) {
                comboBox.setSelectedIndex(index);
                break;
            }
            index++;
        }
    }

    @Override
    public void clearValue() {
        comboBox.setSelectedIndex(0);
    }

    @Override
    public Optional<FormDataValue> getValue() {
        int selIndex = comboBox.getSelectedIndex();
        if(selIndex < 1) {
            return Optional.absent();
        }
        FormDataValue selData = choiceDescriptors.get(selIndex - 1).getValue();
        return Optional.of(selData);
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataValue>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return false;
    }
}