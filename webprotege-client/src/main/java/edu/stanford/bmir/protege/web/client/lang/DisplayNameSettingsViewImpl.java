package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Jul 2018
 */
public class DisplayNameSettingsViewImpl extends Composite implements DisplayNameSettingsView {

    @Nonnull
    private ChangeHandler changeHandler = () -> {};

    interface DisplayNameSettingsViewImplUiBinder extends UiBinder<HTMLPanel, DisplayNameSettingsViewImpl> {

    }

    private static DisplayNameSettingsViewImplUiBinder ourUiBinder = GWT.create(DisplayNameSettingsViewImplUiBinder.class);

    @UiField(provided = true)
    final ValueListFlexEditorImpl<DictionaryLanguage> primaryDisplayNameLanguages;

    @UiField(provided = true)
    final ValueListFlexEditorImpl<DictionaryLanguage> secondaryDisplayNameLanguages;

    @Inject
    public DisplayNameSettingsViewImpl(Provider<DictionaryLanguageDataEditor> editorProvider) {
        this.primaryDisplayNameLanguages = new ValueListFlexEditorImpl<>(editorProvider::get);
        this.secondaryDisplayNameLanguages = new ValueListFlexEditorImpl<>(editorProvider::get);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setPrimaryDisplayNameLanguages(@Nonnull ImmutableList<DictionaryLanguage> languages) {
        primaryDisplayNameLanguages.setValue(languages);
    }

    @Nonnull
    @Override
    public ImmutableList<DictionaryLanguage> getPrimaryDisplayNameLanguages() {
        return ImmutableList.copyOf(primaryDisplayNameLanguages.getValue().orElse(ImmutableList.of()));
    }

    @Override
    public void setSecondaryDisplayNameLanguages(@Nonnull ImmutableList<DictionaryLanguage> languages) {
        secondaryDisplayNameLanguages.setValue(languages);
    }

    @Nonnull
    @Override
    public ImmutableList<DictionaryLanguage> getSecondaryDisplayNameLanguages() {
        return ImmutableList.copyOf(secondaryDisplayNameLanguages.getValue().orElse(ImmutableList.of()));
    }

    @Override
    public void setChangeHandler(@Nonnull ChangeHandler changeHandler) {
        this.changeHandler = checkNotNull(changeHandler);
    }
}