package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
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
    RadioButton optionalRadio;

    @UiField
    RadioButton requiredRadio;

    @UiField(provided = true)
    LanguageMapEditor labelEditor;

    @UiField(provided = true)
    LanguageMapEditor helpEditor;

    @UiField
    ListBox typesComboBox;

    @UiField
    RadioButton nonRepeatableRadio;

    @UiField
    RadioButton repeatableVerticallyRadio;

    @UiField
    RadioButton repeatableHorizontallyRadio;

    @UiField
    Label numberField;

    private final List<String> types = new ArrayList<>();

    @Inject
    public FormElementDescriptorEditorViewImpl(LanguageMapEditor labelEditor,
                                               LanguageMapEditor helpEditor) {
        this.labelEditor = labelEditor;
        this.helpEditor = helpEditor;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFieldEditorContainer() {
        return fieldEditorContainer;
    }

    @Override
    public void setNumber(int number) {
        numberField.setText(Integer.toString(number));
    }

    @Override
    public void setFormElementId(@Nonnull String id) {
        elementIdField.setText(id);
    }

    @Nonnull
    @Override
    public String getFormElementId() {
        return elementIdField.getText();
    }


    @Override
    public void clearOwlProperty() {

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
    public void setOptionality(@Nonnull Optionality optionality) {
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
    public void setOwlProperty(@Nonnull OWLProperty property) {

    }

    @Nonnull
    @Override
    public Optional<OWLProperty> getOwlProperty() {
        return Optional.empty();
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

    @Nonnull
    @Override
    public String getFieldType() {
        return typesComboBox.getSelectedValue();
    }

    @Override
    public void setFieldType(@Nonnull String fieldType) {
        int index = types.indexOf(fieldType);
        if(index == -1) {
            return;
        }
        typesComboBox.setSelectedIndex(index);
    }

    @Override
    public void setAvailableFieldTypes(@Nonnull List<String> fieldTypes) {
        typesComboBox.clear();
        types.clear();
        types.addAll(fieldTypes);
        fieldTypes.forEach(type -> typesComboBox.addItem(type));
        typesComboBox.setSelectedIndex(0);
    }
}
