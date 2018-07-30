package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class PreferredLanguageViewImpl extends Composite implements PreferredLanguageView {

    interface PreferredLanguageViewImplUiBinder extends UiBinder<HTMLPanel, PreferredLanguageViewImpl> {

    }

    private static PreferredLanguageViewImplUiBinder ourUiBinder = GWT.create(PreferredLanguageViewImplUiBinder.class);

    @UiField
    TextBox prefLangField;

    private ChangeHandler changeHandler = () -> {};

    @Inject
    public PreferredLanguageViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public String getLanguage() {
        return prefLangField.getText();
    }

    @Override
    public void setLanguage(@Nonnull String language) {
        prefLangField.setText(checkNotNull(language));
    }

    @Override
    public void setChangeHandler(@Nonnull ChangeHandler handler) {
        this.changeHandler = checkNotNull(handler);
    }

    @UiHandler("prefLangField")
    public void prefLangFieldKeyPress(ValueChangeEvent<String> event) {
        changeHandler.handlePreferredLanguageChanged();
    }
}