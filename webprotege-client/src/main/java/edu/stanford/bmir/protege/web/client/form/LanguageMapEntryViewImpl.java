package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class LanguageMapEntryViewImpl extends Composite implements LanguageMapEntryView {

    interface LanguageMapEntryViewImplUiBinder extends UiBinder<HTMLPanel, LanguageMapEntryViewImpl> {

    }

    private static LanguageMapEntryViewImplUiBinder ourUiBinder = GWT.create(LanguageMapEntryViewImplUiBinder.class);

    @UiField
    TextBox valueField;

    @UiField(provided = true)
    DefaultLanguageEditor langTagEditor;

    @Inject
    public LanguageMapEntryViewImpl(DefaultLanguageEditor langTagEditor) {
        this.langTagEditor = checkNotNull(langTagEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setLangTag(@Nonnull String langTag) {
        langTagEditor.setValue(langTag);
    }

    @Nonnull
    @Override
    public String getLangTag() {
        return langTagEditor.getValue().orElse("");
    }

    @Override
    public void setValue(@Nonnull String value) {
        valueField.setText(value);
    }

    @Nonnull
    @Override
    public String getValue() {
        return valueField.getText();
    }

    @Override
    public void requestFocus() {
        valueField.setFocus(true);
    }

    @Override
    public String getPlaceholder() {
        return valueField.getElement().getAttribute("placeholder");
    }

    @Override
    public void setPlaceholder(String placeholder) {
        valueField.getElement().setAttribute("placeholder", placeholder);
    }
}
