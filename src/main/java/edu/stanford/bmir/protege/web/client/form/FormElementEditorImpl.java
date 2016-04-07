package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueListEditorImpl;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;

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

    private final ValueEditor<FormDataList> delegateEditor;

    public FormElementEditorImpl(ValueEditorFactory<FormDataValue> editorFactory, Repeatability repeatability) {
        initWidget(ourUiBinder.createAndBindUi(this));
        if(repeatability == Repeatability.REPEATABLE) {
            delegateEditor = new RepeatingEditor(new ValueListEditorImpl<>(editorFactory));
        }
        else {
            delegateEditor = new NonRepeatingEditor(editorFactory.createEditor());
        }
        editorHolder.setWidget(delegateEditor);
    }


    @Override
    public void setValue(FormDataList value) {
        delegateEditor.setValue(value);
    }

    @Override
    public void clearValue() {
        delegateEditor.clearValue();
    }

    @Override
    public Optional<FormDataList> getValue() {
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
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Optional<FormDataList>> handler) {
        return delegateEditor.addValueChangeHandler(new ValueChangeHandler<Optional<FormDataList>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<FormDataList>> event) {
                ValueChangeEvent.fire(FormElementEditorImpl.this, getValue());
            }
        });
    }

    @Override
    public boolean isWellFormed() {
        return delegateEditor.isWellFormed();
    }
}