package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.FormDataTuple;
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
    public void setValue(FormDataTuple object) {
        Optional<OWLPrimitiveData> value = object.getSingleValueData();
        if(!value.isPresent()) {
            return;
        }
        int index = 1;
        for(ChoiceDescriptor descriptor : choiceDescriptors) {
            if(descriptor.getData().equals(value.get())) {
                comboBox.setSelectedIndex(index);
                index++;
            }
        }
    }

    @Override
    public void clearValue() {
        comboBox.setSelectedIndex(0);
    }

    @Override
    public Optional<FormDataTuple> getValue() {
        int selIndex = comboBox.getSelectedIndex();
        GWT.log("COM SEL IND " + selIndex);
        if(selIndex < 1) {
            return Optional.absent();
        }
        OWLPrimitiveData selData = choiceDescriptors.get(selIndex - 1).getData();
        GWT.log("SEL DAT " + selData);
        return Optional.of(new FormDataTuple(selData));
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataTuple>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return false;
    }
}