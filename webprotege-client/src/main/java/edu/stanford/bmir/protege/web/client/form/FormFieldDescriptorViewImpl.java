package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.form.field.FieldRun;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class FormFieldDescriptorViewImpl extends Composite implements FormFieldDescriptorView {

    @Nonnull
    private Consumer<FormFieldId> elementIdChangedHandler = (elementId) -> {};

    interface FormFieldDescriptorEditorViewImplUiBinder extends UiBinder<HTMLPanel, FormFieldDescriptorViewImpl> {

    }

    private static FormFieldDescriptorEditorViewImplUiBinder ourUiBinder = GWT.create(
            FormFieldDescriptorEditorViewImplUiBinder.class);

    @UiField
    TextBox elementIdField;

    @UiField
    RadioButton optionalRadio;

    @UiField
    RadioButton requiredRadio;

    @UiField(provided = true)
    LanguageMapEditor labelEditor;

    @UiField(provided = true)
    LanguageMapEditor helpEditor;


    @UiField
    RadioButton nonRepeatableRadio;

    @UiField
    RadioButton repeatableVerticallyRadio;

    @UiField
    RadioButton repeatableHorizontallyRadio;

    @UiField
    RadioButton elementRunStartRadio;

    @UiField
    RadioButton elementRunContinueRadio;

    @UiField(provided = true)
    PrimitiveDataEditor propertyBindingField;

    @UiField(provided = true)
    protected static Counter counter = new Counter();

    @UiField
    SimplePanel fieldViewContainer;

    @Inject
    public FormFieldDescriptorViewImpl(LanguageMapEditor labelEditor,
                                       LanguageMapEditor helpEditor,
                                       PrimitiveDataEditor propertyBindingField) {
        counter.increment();
        this.labelEditor = labelEditor;
        this.helpEditor = helpEditor;
        this.propertyBindingField = propertyBindingField;
        initWidget(ourUiBinder.createAndBindUi(this));
        elementIdField.addValueChangeHandler(event -> {
            FormFieldId formFieldId = FormFieldId.get(elementIdField.getValue());
            elementIdChangedHandler.accept(formFieldId);
        });
    }

    @Override
    public void setFormFieldId(@Nonnull String id) {
        elementIdField.setText(id);
    }

    @Nonnull
    @Override
    public String getFormFieldId() {
        return elementIdField.getText();
    }


    @Override
    public void clearOwlProperty() {
        propertyBindingField.clearValue();
    }

    @Override
    public void setHelp(@Nonnull LanguageMap help) {
        helpEditor.setValue(help);
    }

    @Nonnull
    @Override
    public LanguageMap getHelp() {
        return helpEditor.getValue().orElse(LanguageMap.empty());
    }

    @Override
    public void setLabel(@Nonnull LanguageMap label) {
        labelEditor.setValue(label);
    }

    @Nonnull
    @Override
    public LanguageMap getLabel() {
        return labelEditor.getValue().orElse(LanguageMap.empty());
    }

    @Override
    public void setFieldRun(@Nonnull FieldRun fieldRun) {
        if(fieldRun == FieldRun.CONTINUE) {
            elementRunContinueRadio.setValue(true);
        }
        else {
            elementRunStartRadio.setValue(true);
        }
    }

    @Nonnull
    @Override
    public FieldRun getFieldRun() {
        if(elementRunContinueRadio.getValue()) {
            return FieldRun.CONTINUE;
        }
        else {
            return FieldRun.START;
        }
    }

    @Override
    public void setOptionality(@Nonnull Optionality optionality) {
        GWT.log("[FormFieldDescriptorViewImpl] setOptionality");
        if(optionality == Optionality.OPTIONAL) {
            optionalRadio.setValue(true);
        }
        else {
            requiredRadio.setValue(true);
        }
    }

    @Nonnull
    @Override
    public Optionality getOptionality() {
        return optionalRadio.getValue() ? Optionality.OPTIONAL : Optionality.REQUIRED;
    }

    @Override
    public void setOwlProperty(@Nonnull OWLPropertyData property) {
        propertyBindingField.setValue(property);
    }

    @Nonnull
    @Override
    public Optional<OWLPropertyData> getOwlProperty() {
        return propertyBindingField.getValue()
                .filter(property -> property instanceof OWLPropertyData)
                .map(property -> (OWLPropertyData) property);
    }

    @Override
    public void setRepeatability(@Nonnull Repeatability repeatability) {
        if(repeatability == Repeatability.REPEATABLE_VERTICALLY) {
            repeatableVerticallyRadio.setValue(true);
        }
        else if(repeatability == Repeatability.REPEATABLE_HORIZONTALLY) {
            repeatableHorizontallyRadio.setValue(true);
        }
        else {
            nonRepeatableRadio.setValue(true);
        }
    }

    @Nonnull
    @Override
    public Repeatability getRepeatability() {
        if(repeatableVerticallyRadio.getValue()) {
            return Repeatability.REPEATABLE_VERTICALLY;
        }
        else if(repeatableHorizontallyRadio.getValue()) {
            return Repeatability.REPEATABLE_HORIZONTALLY;
        }
        else {
            return Repeatability.NON_REPEATABLE;
        }
    }

    @Override
    public void setElementIdChangedHandler(@Nonnull Consumer<FormFieldId> handler) {
        this.elementIdChangedHandler = checkNotNull(handler);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFieldDescriptorViewContainer() {
        return fieldViewContainer;
    }
}
