package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.HasDeleteable;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataObject;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/04/16
 */
public class CompositeFieldEditor extends Composite implements ValueEditor<FormDataValue>, HasDeleteable {

    interface CompositeFieldEditorUiBinder extends UiBinder<HTMLPanel, CompositeFieldEditor> {

    }

    private static CompositeFieldEditorUiBinder ourUiBinder = GWT.create(CompositeFieldEditorUiBinder.class);

    private Map<FormElementId, ValueEditor<FormDataValue>> childEditors = new LinkedHashMap<>();

    @UiField
    HTMLPanel container;

    @Inject
    public CompositeFieldEditor() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void addChildEditor(FormElementId id, double flexGrow, double flexShrink, ValueEditor<FormDataValue> childEditor) {
        childEditors.put(id, childEditor);
        childEditor.addValueChangeHandler(valueChangeEvent -> ValueChangeEvent.fire(this, getValue()));
        childEditor.addDirtyChangedHandler(event -> fireEvent(new DirtyChangedEvent()));
        CompositeFieldEditorChildHolder holder = new CompositeFieldEditorChildHolder();
        holder.setFlexGrow(flexGrow);
        holder.setFlexShrink(flexShrink);
        if(flexGrow == flexShrink && flexGrow == 0) {
            holder.setWidth("auto");
        }
        holder.setEditor(childEditor);
        container.add(holder);
    }

    @Override
    public boolean isDeleteable() {
        return true;
    }

    @Override
    public void setValue(FormDataValue value) {
        if(!value.isObject()) {
            clearValue();
            return;
        }
        FormDataObject object = (FormDataObject) value;
        for(FormElementId childId : childEditors.keySet()) {
            ValueEditor<FormDataValue> editor = childEditors.get(childId);
            if(editor != null) {
                Optional<FormDataValue> childValue = object.get(childId.getId());
                if (childValue.isPresent()) {
                    editor.setValue(childValue.get());
                }
                else {
                    editor.clearValue();
                }
            }
        }
    }

    @Override
    public void clearValue() {
        for(ValueEditor<FormDataValue> childEditor : childEditors.values()) {
            childEditor.clearValue();
        }
    }

    @Override
    public Optional<FormDataValue> getValue() {
        GWT.log("[CompositeFieldEditor] Getting values");
        Map<String, FormDataValue> childData = new HashMap<>();
        for(FormElementId childId : childEditors.keySet()) {
            GWT.log("[CompositeFieldEditor] " + childId);
            ValueEditor<FormDataValue> childEditor = childEditors.get(childId);
            if(childEditor != null) {
                Optional<FormDataValue> childValue = childEditor.getValue();
                GWT.log("[CompositeFieldEditor] Value: " + childValue);
                if(childValue.isPresent()) {
                    childData.put(childId.getId(), childValue.get());
                }
            }
        }

        GWT.log("[CompositeFieldEditor] All values: " + childData);
        if(childData.isEmpty()) {
            return Optional.empty();
        }
        FormDataObject theValue = new FormDataObject(childData);
        GWT.log("[CompositeFieldEditor] The value: " + theValue);
        return Optional.of(theValue);
    }

    @Override
    public boolean isDirty() {
        for(ValueEditor<FormDataValue> childEditor : childEditors.values()) {
            if(!childEditor.isDirty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataValue>> valueChangeHandler) {
        return addHandler(valueChangeHandler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
//        for(ValueEditor<FormDataValue> childEditor : childEditors.values()) {
//            if(!childEditor.isWellFormed()) {
//                return false;
//            }
//        }
        return true;
    }
}