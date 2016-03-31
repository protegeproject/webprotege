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
import edu.stanford.bmir.protege.web.shared.form.Tuple;
import edu.stanford.bmir.protege.web.shared.form.FormElementData;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;

import java.util.List;

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

    private final FormElementId formElementId;

    private final Repeatability repeatability;

    private final ValueEditor<List<Tuple>> delegateEditor;

    public FormElementEditorImpl(FormElementId formElementId, ValueEditorFactory<Tuple> editorFactory, Repeatability repeatability) {
        this.formElementId = formElementId;
        this.repeatability = repeatability;
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
    public void setValue(FormElementData value) {
        delegateEditor.setValue(value.getTuples());
    }

    @Override
    public void clearValue() {
        delegateEditor.clearValue();
    }

    @Override
    public Optional<FormElementData> getValue() {
        Optional<List<Tuple>> delegateValue = delegateEditor.getValue();
        if(!delegateValue.isPresent()) {
            return Optional.absent();
        }
        List<Tuple> editedValue = delegateValue.get();
        return Optional.of(new FormElementData(formElementId, editedValue));
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
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Optional<FormElementData>> handler) {
        return delegateEditor.addValueChangeHandler(new ValueChangeHandler<Optional<List<Tuple>>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<List<Tuple>>> event) {
                ValueChangeEvent.fire(FormElementEditorImpl.this, getValue());
            }
        });
    }

    @Override
    public boolean isWellFormed() {
        return delegateEditor.isWellFormed();
    }
}