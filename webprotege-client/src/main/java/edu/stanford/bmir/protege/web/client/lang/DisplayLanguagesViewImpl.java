package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class DisplayLanguagesViewImpl extends Composite implements DisplayLanguagesView {

    interface DisplayLanguagesViewImplUiBinder extends UiBinder<HTMLPanel, DisplayLanguagesViewImpl> {

    }

    private static DisplayLanguagesViewImplUiBinder ourUiBinder = GWT.create(DisplayLanguagesViewImplUiBinder.class);

    @UiField(provided = true)
    ValueListFlexEditorImpl<DictionaryLanguageData> languagesList;

    @Inject
    public DisplayLanguagesViewImpl(@Nonnull Provider<DictionaryLanguageDataEditor> editorProvider) {
        languagesList = new ValueListFlexEditorImpl<>(editorProvider::get);
        languagesList.setEnabled(true);
        languagesList.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}