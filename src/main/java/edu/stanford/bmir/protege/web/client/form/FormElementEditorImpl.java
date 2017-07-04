package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorDirection;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormElementEditorImpl extends Composite implements FormElementEditor {

    interface FormElementEditorImplUiBinder extends UiBinder<HTMLPanel, FormElementEditorImpl> {

    }

    private static FormElementEditorImplUiBinder ourUiBinder = GWT.create(FormElementEditorImplUiBinder.class);



    @UiField
    SimplePanel editorHolder;

    private final ValueEditor<FormDataValue> delegateEditor;

    public FormElementEditorImpl(ValueEditorFactory<FormDataValue> editorFactory, Repeatability repeatability) {
        initWidget(ourUiBinder.createAndBindUi(this));
        if(repeatability == Repeatability.REPEATABLE_HORIZONTAL || repeatability == Repeatability.REPEATABLE_VERTICAL) {
            ValueListFlexEditorImpl<FormDataValue> delegate = new ValueListFlexEditorImpl<>(editorFactory);
            delegateEditor = new RepeatingEditor(delegate);
            if (repeatability == Repeatability.REPEATABLE_HORIZONTAL) {
                delegate.setDirection(ValueListFlexEditorDirection.ROW);
            }
            else {
                delegate.setDirection(ValueListFlexEditorDirection.COLUMN);
            }
        }
        else {
            delegateEditor = new NonRepeatingEditor(editorFactory.createEditor());
        }
        editorHolder.setWidget(delegateEditor);
    }


    @Override
    public void setValue(FormDataValue value) {
        delegateEditor.setValue(value);
    }

    @Override
    public void clearValue() {
        delegateEditor.clearValue();
    }

    @Override
    public Optional<FormDataValue> getValue() {
        return delegateEditor.getValue();
    }

    @Override
    public boolean isDirty() {
        return delegateEditor.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return delegateEditor.addDirtyChangedHandler(handler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Optional<FormDataValue>> handler) {
        return delegateEditor.addValueChangeHandler(event -> ValueChangeEvent.fire(FormElementEditorImpl.this, getValue()));
    }

    @Override
    public boolean isWellFormed() {
        return delegateEditor.isWellFormed();
    }
}