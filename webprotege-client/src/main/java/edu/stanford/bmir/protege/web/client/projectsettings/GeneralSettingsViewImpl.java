package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2018
 */
public class GeneralSettingsViewImpl extends Composite implements GeneralSettingsView {

    interface GeneralSettingsViewImplUiBinder extends UiBinder<HTMLPanel, GeneralSettingsViewImpl> {

    }

    private static GeneralSettingsViewImplUiBinder ourUiBinder = GWT.create(GeneralSettingsViewImplUiBinder.class);

    @UiField
    TextBox displayNameField;

    @UiField
    TextArea descriptionField;

    @Inject
    public GeneralSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setDisplayName(@Nonnull String displayName) {
        displayNameField.setValue(displayName);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return displayNameField.getValue().trim();
    }

    @Override
    public void setDescription(@Nonnull String description) {
        descriptionField.setValue(description);
    }

    @Nonnull
    @Override
    public String getDescription() {
        return descriptionField.getValue().trim();
    }
}