package edu.stanford.bmir.protege.web.client.irigen.supplied;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsEditor;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsId;
import edu.stanford.bmir.protege.web.shared.irigen.supplied.SuppliedSuffixSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class SuppliedSuffixSettingsEditor extends Composite implements SuffixSettingsEditor<SuppliedSuffixSettings> {

    interface SuffixGeneratorSettingsEditorUiBinder extends UiBinder<HTMLPanel, SuppliedSuffixSettingsEditor> {

    }

    private static SuffixGeneratorSettingsEditorUiBinder ourUiBinder = GWT.create(SuffixGeneratorSettingsEditorUiBinder.class);

    public SuppliedSuffixSettingsEditor() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public SuffixSettingsId getSupportedSuffixSettingsId() {
        return SuppliedSuffixSettings.Id;
    }

    @Override
    public void setSettings(SuppliedSuffixSettings settings) {
    }

    @Override
    public SuppliedSuffixSettings getSettings() {
        return new SuppliedSuffixSettings();
    }
}