package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.partitioningBy;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class DefaultDisplayNameSettingsViewImpl extends Composite implements DefaultDisplayNameSettingsView {

    private static DisplayLanguagesViewImplUiBinder ourUiBinder = GWT.create(DisplayLanguagesViewImplUiBinder.class);

    @UiField(provided = true)
    ValueListFlexEditorImpl<DictionaryLanguageData> languagesList;

    @UiField
    Button resetButton;

    @UiField
    CheckBox fallbackCheckBox;

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
    public ImmutableList<DictionaryLanguageData> getPrimaryLanguages() {
        ImmutableList<DictionaryLanguageData> specifiedList = languagesList.getValue().map(ImmutableList::copyOf).orElse(ImmutableList.of());
        if (fallbackCheckBox.getValue()) {
            return Streams.concat(specifiedList.stream(),
                                  Stream.of(DictionaryLanguageData.localName())).collect(toImmutableList());
        }
        else {
            return specifiedList;
        }
    }

    @Override
    public void setPrimaryLanguages(@Nonnull List<DictionaryLanguageData> primaryLanguages) {
        Map<Boolean, ImmutableList<DictionaryLanguageData>> langs = primaryLanguages.stream()
                                                                                    .collect(partitioningBy(DictionaryLanguageData::isAnnotationBased,
                                                                                                            toImmutableList()));
        languagesList.setValue(langs.getOrDefault(true, ImmutableList.of()));
        fallbackCheckBox.setValue(!langs.get(false).isEmpty());
    }

    @Override
    public void setResetLanguagesHandler(@Nonnull ResetLanguagesHandler handler) {
        this.resetLanguagesHandler = checkNotNull(handler);
    }

    interface DisplayLanguagesViewImplUiBinder extends UiBinder<HTMLPanel, DefaultDisplayNameSettingsViewImpl> {

    }
}