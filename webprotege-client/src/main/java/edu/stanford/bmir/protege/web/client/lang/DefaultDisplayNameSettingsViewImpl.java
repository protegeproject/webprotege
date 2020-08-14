package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class DefaultDisplayNameSettingsViewImpl extends Composite implements DefaultDisplayNameSettingsView {

    private static DisplayLanguagesViewImplUiBinder ourUiBinder = GWT.create(DisplayLanguagesViewImplUiBinder.class);

    @UiField(provided = true)
    ValueListFlexEditorImpl<DictionaryLanguage> languagesList;

    @UiField
    Button resetButton;

    private ResetLanguagesHandler resetLanguagesHandler = () -> {};

    @Inject
    public DefaultDisplayNameSettingsViewImpl(@Nonnull Provider<DictionaryLanguageDataEditor> editorProvider) {
        languagesList = new ValueListFlexEditorImpl<>(editorProvider::get);
        languagesList.setEnabled(true);
        languagesList.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("resetButton")
    protected void handleResetButtonClicked(ClickEvent event) {
        resetLanguagesHandler.handleResetLanguages();
    }

    @Nonnull
    @Override
    public ImmutableList<DictionaryLanguage> getPrimaryLanguages() {
        return languagesList.getValue().map(ImmutableList::copyOf).orElse(ImmutableList.of());
    }

    @Override
    public void setPrimaryLanguages(@Nonnull List<DictionaryLanguage> primaryLanguages) {
        languagesList.setValue(primaryLanguages);
    }

    @Override
    public void setResetLanguagesHandler(@Nonnull ResetLanguagesHandler handler) {
        this.resetLanguagesHandler = checkNotNull(handler);
    }

    interface DisplayLanguagesViewImplUiBinder extends UiBinder<HTMLPanel, DefaultDisplayNameSettingsViewImpl> {

    }
}