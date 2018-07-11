package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jul 2018
 */
public class GlobalPermissionSettingsViewImpl extends Composite implements GlobalPermissionSettingsView {

    interface GlobalPermissionSettingsViewImplUiBinder extends UiBinder<HTMLPanel, GlobalPermissionSettingsViewImpl> {

    }

    private static GlobalPermissionSettingsViewImplUiBinder ourUiBinder = GWT.create(GlobalPermissionSettingsViewImplUiBinder.class);

    @UiField
    CheckBox accountCreationEnabledCheckBox;

    @UiField
    CheckBox projectCreationEnabledCheckBox;

    @UiField
    CheckBox projectUploadEnabledCheckBox;

    @UiField
    PlaceholderTextBox maxUploadSize;

    @Inject
    public GlobalPermissionSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public boolean isAccountCreationAllowed() {
        return accountCreationEnabledCheckBox.getValue();
    }

    @Override
    public void setAccountCreationAllowed(boolean allowed) {
        accountCreationEnabledCheckBox.setValue(allowed);
    }

    @Override
    public boolean isProjectCreationAllowed() {
        return projectCreationEnabledCheckBox.getValue();
    }

    @Override
    public void setProjectCreationAllowed(boolean allowed) {
        projectCreationEnabledCheckBox.setValue(allowed);
    }

    @Override
    public boolean isProjectUploadAllowed() {
        return projectUploadEnabledCheckBox.getValue();
    }

    @Override
    public void setProjectUploadAllowed(boolean allowed) {
        projectUploadEnabledCheckBox.setValue(allowed);
    }
    @Override
    public String getMaxUploadSize() {
        return maxUploadSize.getText().trim();
    }

    @Override
    public void setMaxUploadSize(String size) {
        maxUploadSize.setText(size);
    }
}