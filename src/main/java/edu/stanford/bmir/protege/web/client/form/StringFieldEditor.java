package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.LineMode;
import edu.stanford.bmir.protege.web.shared.form.field.StringType;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class StringFieldEditor extends Composite implements ValueEditor<FormDataValue> {

    interface StringFieldEditorUiBinder extends UiBinder<HTMLPanel, StringFieldEditor> {

    }

    private static StringFieldEditorUiBinder ourUiBinder = GWT.create(StringFieldEditorUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl editor;

    @UiField(provided = true)
    DefaultLanguageEditor languageEditor;

    private Optional<String> pattern = Optional.absent();

    private Optional<String> patternViolationErrorMessage = Optional.absent();

    private Optional<String> langPattern = Optional.absent();

    private Optional<String> langPatternViolationErrorMessage = Optional.absent();

    @Inject
    public StringFieldEditor(Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        this.editor = (PrimitiveDataEditorImpl) primitiveDataEditorProvider.get();
        this.languageEditor = (DefaultLanguageEditor) editor.getLanguageEditor();
        initWidget(ourUiBinder.createAndBindUi(this));
        editor.setAllowedTypes(Collections.singleton(PrimitiveType.LITERAL));
        editor.addValueChangeHandler(new ValueChangeHandler<Optional<OWLPrimitiveData>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
                validateInput();
                ValueChangeEvent.fire(StringFieldEditor.this, getValue());
            }
        });
        editor.setFreshEntitiesSuggestStrategy(new NullFreshEntitySuggestStrategy());
        editor.setShowLinksForEntities(false);
    }

    public void setPlaceholder(String placeholder) {
        editor.setPlaceholder(placeholder);
    }

    public void setPattern(String pattern) {
        this.pattern = Optional.of(checkNotNull(pattern));
    }

    public void setPatternViolationErrorMessage(String patternViolationErrorMessage) {
        this.patternViolationErrorMessage = Optional.of(checkNotNull(patternViolationErrorMessage));
    }

    private void validateInput() {
        if(pattern.isPresent()) {
            RegExp regExp = RegExp.compile(pattern.get());
            if(!regExp.test(editor.getText().trim())) {
                if (patternViolationErrorMessage.isPresent()) {
                    editor.setTitle(patternViolationErrorMessage.get());
                }
            }
            else {
                editor.getElement().getStyle().clearBorderColor();
            }

        }
        if(langPattern.isPresent()) {

        }

    }

    public void setStringType(StringType stringType) {
        if(stringType == StringType.SIMPLE_STRING) {
            languageEditor.setVisible(false);
        }
        else {
            languageEditor.setVisible(true);
        }
    }

    public void setLineMode(LineMode lineMode) {
        if (lineMode == LineMode.MULTI_LINE) {
            editor.setMode(PrimitiveDataEditorView.Mode.MULTI_LINE);
        }
        else {
            editor.setMode(PrimitiveDataEditorView.Mode.SINGLE_LINE);
        }
    }

    @Override
    public void setValue(FormDataValue object) {
        Optional<OWLLiteral> primitive = object.asLiteral();
        if(!primitive.isPresent()) {
            clearValue();
        }
        else {
            editor.setValue(new OWLLiteralData(primitive.get()));
        }
    }

    @Override
    public void clearValue() {
        editor.clearValue();
    }

    @Override
    public Optional<FormDataValue> getValue() {
        Optional<OWLPrimitiveData> editedValue = editor.getValue();
        if(!editedValue.isPresent()) {
            return Optional.<FormDataValue>absent();
        }
        OWLLiteralData literalData = (OWLLiteralData) editedValue.get();
        return Optional.<FormDataValue>of(FormDataPrimitive.get(literalData.getLiteral()));
    }

    @Override
    public boolean isDirty() {
        return true;
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
        return true;
    }
}