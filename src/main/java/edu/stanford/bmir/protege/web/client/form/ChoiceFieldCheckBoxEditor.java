package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.Tuple;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class ChoiceFieldCheckBoxEditor extends Composite implements ChoiceFieldEditor {

    private ValueChangeHandler<Boolean> checkBoxValueChangedHandler;

    interface ChoiceFieldCheckBoxEditorUiBinder extends UiBinder<HTMLPanel, ChoiceFieldCheckBoxEditor> {

    }

    private static ChoiceFieldCheckBoxEditorUiBinder ourUiBinder = GWT.create(ChoiceFieldCheckBoxEditorUiBinder.class);

    @UiField
    HTMLPanel container;

    private final Map<CheckBox, ChoiceDescriptor> checkBoxes = new HashMap<>();

    public ChoiceFieldCheckBoxEditor() {
        initWidget(ourUiBinder.createAndBindUi(this));
        checkBoxValueChangedHandler = new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                ValueChangeEvent.fire(ChoiceFieldCheckBoxEditor.this, getValue());
            }
        };
    }

    @Override
    public void setChoices(List<ChoiceDescriptor> choices) {
        container.clear();
        checkBoxes.clear();
        for(ChoiceDescriptor choiceDescriptor : choices) {
            CheckBox checkBox = new CheckBox(new SafeHtmlBuilder().appendHtmlConstant(choiceDescriptor.getLabel()).toSafeHtml());
            checkBoxes.put(checkBox, choiceDescriptor);
            container.add(checkBox);
            checkBox.getElement().getStyle().setDisplay(Style.Display.BLOCK);
            checkBox.addValueChangeHandler(checkBoxValueChangedHandler);
        }
    }

    @Override
    public void setValue(Tuple object) {
        clearValue();
        for(OWLPrimitiveData data : object.getData()) {
            for(CheckBox checkBox : checkBoxes.keySet()) {
                if(checkBoxes.get(checkBox).getData().equals(data)) {
                    checkBox.setValue(true);
                }
            }
        }
    }

    @Override
    public void clearValue() {
        for(CheckBox checkBox : checkBoxes.keySet()) {
            checkBox.setValue(false);
        }
    }

    @Override
    public Optional<Tuple> getValue() {
        List<OWLPrimitiveData> selected = new ArrayList<>();
        for(CheckBox checkBox : checkBoxes.keySet()) {
            if(checkBox.getValue()) {
                selected.add(checkBoxes.get(checkBox).getData());
            }
        }

        return Optional.<Tuple>of(new Tuple(selected));
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<Tuple>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }
}