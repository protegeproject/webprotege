package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.form.field.LineMode;
import edu.stanford.bmir.protege.web.shared.form.field.StringType;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class TextControlDescriptorViewImpl extends Composite implements TextControlDescriptorView {

    interface TextControlDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, TextControlDescriptorViewImpl> {

    }

    private static TextControlDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            TextControlDescriptorViewImplUiBinder.class);

    @UiField
    RadioButton simpleString;

    @UiField
    RadioButton singleLineMode;

    @UiField
    TextBox patternField;

    @UiField(provided = true)
    LanguageMapEditor patternViolationMessageEditor;

    @UiField(provided = true)
    LanguageMapEditor placeholderEditor;

    @UiField
    RadioButton multiLineMode;

    @UiField
    RadioButton langString;

    @UiField(provided = true)
    protected static Counter counter = new Counter();

    @Inject
    public TextControlDescriptorViewImpl(@Nonnull LanguageMapEditor placeholderEditor,
                                         @Nonnull LanguageMapEditor patternViolationMessageEditor) {
        this.placeholderEditor = checkNotNull(placeholderEditor);
        this.patternViolationMessageEditor = checkNotNull(patternViolationMessageEditor);
        counter.increment();
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setStringType(@Nonnull StringType stringType) {
        if(stringType == StringType.SIMPLE_STRING) {
            simpleString.setValue(true);
        }
        else {
            langString.setValue(true);
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
        if(lineMode == LineMode.SINGLE_LINE) {
            singleLineMode.setValue(true);
        }
        else {
            multiLineMode.setValue(true);
        }
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
        patternViolationMessageEditor.setValue(patternViolationMessage);
    }

    @Nonnull
    @Override
    public LanguageMap getPatternViolationMessage() {
        return patternViolationMessageEditor.getValue().orElse(LanguageMap.empty());
    }

    @Override
    public void setPlaceholder(@Nonnull LanguageMap languageMap) {
        placeholderEditor.setValue(languageMap);
    }

    @Nonnull
    @Override
    public LanguageMap getPlaceholder() {
        return placeholderEditor.getValue().orElse(LanguageMap.empty());
    }
}
