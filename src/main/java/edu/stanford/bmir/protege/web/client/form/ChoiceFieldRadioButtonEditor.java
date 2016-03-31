package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.FormDataTuple;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;

import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class ChoiceFieldRadioButtonEditor extends Composite implements ChoiceFieldEditor {

    private static int nameCounter = 0;

    private ValueChangeHandler<Boolean> radioButtonValueChangedHandler;

    interface ChoiceFieldRadioButtonEditorUiBinder extends UiBinder<HTMLPanel, ChoiceFieldRadioButtonEditor> {

    }

    private static ChoiceFieldRadioButtonEditorUiBinder ourUiBinder = GWT.create(ChoiceFieldRadioButtonEditorUiBinder.class);

    @UiField
    HTMLPanel container;

    private Map<RadioButton, ChoiceDescriptor> choiceButtons = new HashMap<>();

    public ChoiceFieldRadioButtonEditor() {
        initWidget(ourUiBinder.createAndBindUi(this));
        radioButtonValueChangedHandler = new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                ValueChangeEvent.fire(ChoiceFieldRadioButtonEditor.this, getValue());
            }
        };
    }

    @Override
    public void setChoices(List<ChoiceDescriptor> choices) {
        container.clear();
        choiceButtons.clear();
        nameCounter++;
        for(ChoiceDescriptor descriptor : choices) {
            RadioButton radioButton = new RadioButton("Choice" + nameCounter, new SafeHtmlBuilder().appendHtmlConstant(descriptor.getLabel()).toSafeHtml());
            radioButton.addValueChangeHandler(radioButtonValueChangedHandler);
            container.add(radioButton);
            choiceButtons.put(radioButton, descriptor);
        }
    }

    @Override
    public void setValue(FormDataTuple dataTuple) {
        Optional<OWLPrimitiveData> val = dataTuple.getSingleValueData();
        if(!val.isPresent()) {
            clearValue();
            return;
        }
        for(RadioButton radioButton : choiceButtons.keySet()) {
            ChoiceDescriptor choiceDescriptor = choiceButtons.get(radioButton);
            if(choiceDescriptor.getData().equals(val.get())) {
                radioButton.setValue(true);
                break;
            }
        }
    }

    @Override
    public void clearValue() {
        for(RadioButton radioButton : choiceButtons.keySet()) {
            radioButton.setValue(false);
        }
    }

    @Override
    public Optional<FormDataTuple> getValue() {
        for(RadioButton radioButton : choiceButtons.keySet()) {
            if(radioButton.getValue()) {
                ChoiceDescriptor descriptor = choiceButtons.get(radioButton);
                return Optional.<FormDataTuple>of(new FormDataTuple(descriptor.getData()));
            }
        }
        return Optional.<FormDataTuple>absent();
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
        return true;
    }
}