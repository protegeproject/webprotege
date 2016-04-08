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
import com.google.gwt.user.client.ui.SuggestBox;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.inject.WebProtegeClientInjector;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestOracle;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/04/16
 */
public class ClassNameFieldEditor extends Composite implements ValueEditor<FormDataValue> {

    interface ClassNameFieldEditorUiBinder extends UiBinder<HTMLPanel, ClassNameFieldEditor> {

    }

    private static ClassNameFieldEditorUiBinder ourUiBinder = GWT.create(ClassNameFieldEditorUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl editor;

    public ClassNameFieldEditor(ProjectId projectId) {
        editor = WebProtegeClientInjector.getPrimitiveDataEditor(projectId);
        initWidget(ourUiBinder.createAndBindUi(this));
        editor.addValueChangeHandler(new ValueChangeHandler<Optional<OWLPrimitiveData>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
                ValueChangeEvent.fire(ClassNameFieldEditor.this, getValue());
            }
        });
        editor.setAutoSelectSuggestions(true);
        editor.setClassesAllowed(true);
    }

    @Override
    public void setValue(FormDataValue object) {
//        Optional<OWLClassData> classData = object.asOWLClassData();
//        if(classData.isPresent()) {
//            editor.setValue(classData.get());
//        }
//        else {
//            editor.clearValue();
//        }
    }

    @Override
    public void clearValue() {
        editor.clearValue();
    }

    @Override
    public Optional<FormDataValue> getValue() {
        Optional<OWLPrimitiveData> value = editor.getValue();
//        if(!value.isPresent()) {
            return Optional.absent();
//        }
//        OWLPrimitiveData theValue = value.get();
//        if(!(theValue instanceof OWLClassData)) {
//            return Optional.absent();
//        }
//        return Optional.<FormDataValue>of(FormDataPrimitive.get((OWLClassData) value.get()));
    }

    @Override
    public boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return editor.addDirtyChangedHandler(handler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataValue>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return editor.isWellFormed();
    }
}