package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jul 2018
 */
public class EmailNotificationSettingsViewImpl extends Composite implements EmailNotificationSettingsView {

    interface EmailNotificationSettingsViewImplUiBinder extends UiBinder<HTMLPanel, EmailNotificationSettingsViewImpl> {

    }

    private static EmailNotificationSettingsViewImplUiBinder ourUiBinder = GWT.create(EmailNotificationSettingsViewImplUiBinder.class);

    @UiField
    CheckBox emailNotificationsEnabledCheckBox;

    @Inject
    public EmailNotificationSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @Override
    public boolean isNotificationEmailsEnabled() {
        return emailNotificationsEnabledCheckBox.getValue();
    }

    @Override
    public void setNotificationEmailsEnabled(boolean allowed) {
        emailNotificationsEnabledCheckBox.setValue(allowed);
    }


}