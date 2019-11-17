package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.form.LanguageMapEditor;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class FormElementDescriptorEditorViewImpl extends Composite implements FormElementDescriptorEditorView {

    interface FormElementDescriptorEditorViewImplUiBinder extends UiBinder<HTMLPanel, FormElementDescriptorEditorViewImpl> {

    }

    private static FormElementDescriptorEditorViewImplUiBinder ourUiBinder = GWT.create(
            FormElementDescriptorEditorViewImplUiBinder.class);

    @UiField
    SimplePanel fieldEditorContainer;

    @UiField
    TextBox elementIdField;

    @UiField
    CheckBox optionalCheckBox;

    @UiField
    CheckBox requiredCheckBox;

    @UiField
    LanguageMapEditor labelEditor;

    @Inject
    public FormElementDescriptorEditorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFieldEditorContainer() {
        return fieldEditorContainer;
    }

    @Override
    public void setFormElementId(@Nonnull String id) {

    }

    @Nonnull
    @Override
    public String getFormElementId() {
        return null;
    }


    @Override
    public void clearOwlProperty() {

    }

    @Override
    public void setHelp(@Nonnull LanguageMap help) {

    }

    @Nonnull
    @Override
    public LanguageMap getHelp() {
        return null;
    }

    @Override
    public void setLabel(@Nonnull LanguageMap label) {

    }

    @Nonnull
    @Override
    public LanguageMap getLabel() {
        return null;
    }

    @Override
    public void setOptionality(@Nonnull Optionality optionality) {

    }

    @Nonnull
    @Override
    public Optionality getOptionality() {
        return null;
    }

    @Override
    public void setOwlProperty(@Nonnull OWLProperty property) {

    }

    @Nonnull
    @Override
    public Optional<OWLProperty> getOwlProperty() {
        return Optional.empty();
    }

    @Override
    public void setRepeatability(@Nonnull Repeatability repeatability) {

    }

    @Nonnull
    @Override
    public Repeatability getRepeatability() {
        return null;
    }

    @Nonnull
    @Override
    public String getFieldType() {
        return null;
    }

    @Override
    public void setFieldType(@Nonnull String fieldType) {

    }

    @Override
    public void setAvailableFieldTypes(@Nonnull List<String> fieldTypes) {

    }
}
