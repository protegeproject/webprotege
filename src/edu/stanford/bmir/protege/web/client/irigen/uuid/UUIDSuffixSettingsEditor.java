package edu.stanford.bmir.protege.web.client.irigen.uuid;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsEditor;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsId;
import edu.stanford.bmir.protege.web.shared.irigen.uuid.UUIDSuffixSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class UUIDSuffixSettingsEditor extends Composite implements SuffixSettingsEditor<UUIDSuffixSettings> {

    interface UUIDSuffixSettingsEditorUiBinder extends UiBinder<HTMLPanel, UUIDSuffixSettingsEditor> {

    }

    private static UUIDSuffixSettingsEditorUiBinder ourUiBinder = GWT.create(UUIDSuffixSettingsEditorUiBinder.class);

    @UiField
    protected HasText labelLangEditor;

    public UUIDSuffixSettingsEditor() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public SuffixSettingsId getSupportedSuffixSettingsId() {
        return UUIDSuffixSettings.Id;
    }

    @Override
    public void setSettings(UUIDSuffixSettings settings) {
        labelLangEditor.setText(settings.getLabelLang().or(""));
    }

    @Override
    public UUIDSuffixSettings getSettings() {
        return new UUIDSuffixSettings(getLabelLang());
    }

    private Optional<String> getLabelLang() {
        String labelLangText = getLabelLangText();
        return labelLangText.isEmpty() ? Optional.<String>absent() : Optional.of(labelLangText);
    }

    private String getLabelLangText() {
        return labelLangEditor.getText().trim();
    }


}