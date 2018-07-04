package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2018
 */
public class SlackWebhookSettingsViewImpl extends Composite implements SlackWebhookSettingsView {

    interface SlackWebhookSettingsViewImplUiBinder extends UiBinder<HTMLPanel, SlackWebhookSettingsViewImpl> {

    }

    private static SlackWebhookSettingsViewImplUiBinder ourUiBinder = GWT.create(SlackWebhookSettingsViewImplUiBinder.class);

    @UiField
    TextBox slackPayloadUrl;

    @Inject
    public SlackWebhookSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setWebhookUrl(@Nonnull String url) {
        slackPayloadUrl.setValue(checkNotNull(url));
    }

    @Nonnull
    @Override
    public String getWebhookrUrl() {
        return slackPayloadUrl.getValue().trim();
    }
}