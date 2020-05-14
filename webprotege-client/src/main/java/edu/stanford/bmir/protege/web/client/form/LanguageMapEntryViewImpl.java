package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.swing.event.ChangeEvent;

import java.util.function.Consumer;

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

    private Consumer<String> valueChangedHandler = value -> {};

    private Consumer<String> langTagChangedHandler = value -> {};

    @Inject
    public LanguageMapEntryViewImpl(DefaultLanguageEditor langTagEditor) {
        this.langTagEditor = checkNotNull(langTagEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
        valueField.addValueChangeHandler(event -> {
            updateLangTagErrorBorder();
            valueChangedHandler.accept(getValue());
        });
        langTagEditor.addValueChangeHandler(event -> {
            updateLangTagErrorBorder();
            langTagChangedHandler.accept(getLangTag());

        });
    }

    public void updateLangTagErrorBorder() {
        if(valueField.getValue().trim().isEmpty()) {
            langTagEditor.removeStyleName(WebProtegeClientBundle.BUNDLE.style().errorBorderColor());
        }
        else {
            if(langTagEditor.getValue().orElse("").isEmpty()) {
                langTagEditor.addStyleName(WebProtegeClientBundle.BUNDLE.style().errorBorderColor());
            }
            else {

                langTagEditor.removeStyleName(WebProtegeClientBundle.BUNDLE.style().errorBorderColor());
            }
        }
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
    public void setValueChangedHandler(Consumer<String> valueConsumer) {
        this.valueChangedHandler = checkNotNull(valueConsumer);
    }

    @Override
    public void setLangTagChangedHandler(Consumer<String> langTagConsumer) {
        this.langTagChangedHandler = checkNotNull(langTagConsumer);
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
