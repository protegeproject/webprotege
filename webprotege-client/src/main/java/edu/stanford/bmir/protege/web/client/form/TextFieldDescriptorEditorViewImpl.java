package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.shared.form.field.LineMode;
import edu.stanford.bmir.protege.web.shared.form.field.StringType;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class TextFieldDescriptorEditorViewImpl extends Composite implements TextFieldDescriptorEditorView {

    interface TextFieldDescriptorEditorViewImplUiBinder extends UiBinder<HTMLPanel, TextFieldDescriptorEditorViewImpl> {

    }

    private static TextFieldDescriptorEditorViewImplUiBinder ourUiBinder = GWT.create(
            TextFieldDescriptorEditorViewImplUiBinder.class);

    @UiField
    RadioButton simpleString;

    @UiField
    RadioButton singleLineMode;

    @UiField
    TextBox patternField;

    @Inject
    public TextFieldDescriptorEditorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setStringType(@Nonnull StringType stringType) {
        if(stringType == StringType.SIMPLE_STRING) {
            simpleString.setValue(true);
        }
        else {
            simpleString.setValue(true);
        }
    }

    @Nonnull
    @Override
    public StringType getStringType() {
        if(simpleString.getValue()) {
            return StringType.SIMPLE_STRING;
        }
        else {
            return StringType.LANG_STRING;
        }
    }

    @Override
    public void setLineMode(@Nonnull LineMode lineMode) {

    }

    @Nonnull
    @Override
    public LineMode getLineMode() {
        if(singleLineMode.getValue()) {
            return LineMode.SINGLE_LINE;
        }
        else {
            return LineMode.MULTI_LINE;
        }
    }

    @Override
    public void setPattern(@Nonnull String pattern) {
        patternField.setText(pattern);
    }

    @Nonnull
    @Override
    public String getPattern() {
        return patternField.getText();
    }

    @Override
    public void setPatternViolationMessage(@Nonnull LanguageMap patternViolationMessage) {

    }

    @Nonnull
    @Override
    public LanguageMap getPatternViolationMessage() {
        return LanguageMap.empty();
    }

    @Override
    public void setPlaceholder(@Nonnull LanguageMap languageMap) {

    }

    @Nonnull
    @Override
    public LanguageMap getPlaceholder() {
        return null;
    }
}
