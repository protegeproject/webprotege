package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jul 2018
 */
public class SystemDetailsViewImpl extends Composite implements SystemDetailsView {

    interface SystemDetailsViewImplUiBinder extends UiBinder<HTMLPanel, SystemDetailsViewImpl> {

    }

    private static SystemDetailsViewImplUiBinder ourUiBinder = GWT.create(SystemDetailsViewImplUiBinder.class);

    @UiField
    PlaceholderTextBox applicationNameField;

    @UiField
    PlaceholderTextBox systemNotificationEmailAddressField;

    @Inject
    public SystemDetailsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public String getApplicationName() {
        return new SafeHtmlBuilder().appendEscaped(applicationNameField.getText().trim()).toSafeHtml().asString();
    }

    @Override
    public void setApplicationName(@Nonnull String applicationName) {
        applicationNameField.setText(applicationName.trim());
    }

    @Nonnull
    @Override
    public String getSystemNotificationEmailAddress() {
        return systemNotificationEmailAddressField.getText().trim();
    }

    @Override
    public void setSystemNotificationEmailAddress(@Nonnull String emailAddress) {
        systemNotificationEmailAddressField.setText(emailAddress.trim());
    }
}